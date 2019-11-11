package io.skymind.pathmind.ui.views.experiment.components;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;

import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.utils.PolicyBusEventUtils;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.ui.components.SearchBox;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.ui.views.policy.filter.PolicyFilter;
import io.skymind.pathmind.utils.DateAndTimeUtils;
import reactor.core.publisher.Flux;

@Component
public class TrainingsListPanel extends VerticalLayout {
    private static Logger log = LogManager.getLogger(TrainingsListPanel.class);
    private SearchBox<Policy> searchBox;
    private Grid<Policy> grid;

    private Experiment experiment;

    private Flux<PathmindBusEvent> consumer;

    public TrainingsListPanel(Flux<PathmindBusEvent> consumer) {
        this.consumer = consumer;

        setupGrid();
        setupSearchBox();

        add(getTitleAndSearchBoxBar());
        add(grid);

        // Always force at least one item to be selected.
        ((GridSingleSelectionModel<Policy>) grid.getSelectionModel()).setDeselectAllowed(false);

        setSizeFull();
    }

    private void setupGrid() {
        grid = new Grid<>();
        grid.addColumn(policy -> PolicyUtils.getRunStatus(policy))
                .setHeader("Status")
                .setAutoWidth(true)
                .setSortable(true);

        Grid.Column<Policy> startedColumn = grid.addColumn(new LocalDateTimeRenderer<>(Policy::getStartedAt, DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER))
                .setComparator(Comparator.comparing(Policy::getStartedAt, Comparator.nullsFirst(Comparator.naturalOrder())))
                .setHeader("Started")
                .setAutoWidth(true)
                .setSortable(true);

        grid.addColumn(new LocalDateTimeRenderer<>(policy -> PolicyUtils.getRunCompletedTime(policy), DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER))
                .setComparator(Comparator.comparing(policy -> PolicyUtils.getRunCompletedTime(policy)))
                .setHeader("Completed")
                .setAutoWidth(true)
                .setSortable(true);

        grid.addColumn(policy -> PolicyUtils.getLastScore(policy))
                .setHeader("Score")
                .setAutoWidth(true)
                .setSortable(true);

        grid.addColumn(policy -> PolicyUtils.getParsedPolicyName(policy))
                .setHeader("Policy")
                .setAutoWidth(true)
                .setSortable(true);

        grid.addColumn(policy -> policy.getRun().getRunTypeEnum())
                .setHeader("Run Type")
                .setAutoWidth(true)
                .setSortable(true);

        grid.addColumn(policy -> policy.getAlgorithmEnum())
                .setHeader("Algorithm")
                .setAutoWidth(true)
                .setSortable(true);

        grid.addColumn(Policy::getNotes)
                .setHeader("Notes")
                .setAutoWidth(true)
                .setSortable(true);

        grid.sort(Arrays.asList(
                new GridSortOrder<Policy>(startedColumn, SortDirection.DESCENDING)));
    }

    public void addSelectionListener(Consumer<Policy> consumer) {
        grid.addSelectionListener(selectionPolicy ->
                selectionPolicy.getFirstSelectedItem().ifPresent(p -> consumer.accept(p)));
    }

    private HorizontalLayout getTitleAndSearchBoxBar() {
        return GuiUtils.getTitleAndSearchBoxBar(
                "Trainings",
                searchBox);
    }

    private void setupSearchBox() {
        searchBox = new SearchBox(grid, new PolicyFilter(), true);
    }

    public SearchBox getSearchBox() {
        return searchBox;
    }

    private void subscribeToEventBus(UI ui, Flux<PathmindBusEvent> consumer) {
        PolicyBusEventUtils.consumerBusEventBasedOnExperiment(
                consumer,
                () -> getExperiment(),
                updatedPolicy -> PushUtils.push(ui, () -> updatedGrid(updatedPolicy)));
    }

    private void updatedGrid(Policy updatedPolicy) {
        experiment.getPolicies().stream()
                .filter(policy -> policy.getId() == updatedPolicy.getId())
                .findAny()
                .ifPresentOrElse(
                        policy -> {
                            replacePolicy(updatedPolicy);
                            grid.getDataProvider().refreshItem(updatedPolicy);
                        },
                        () -> {
                            experiment.getPolicies().add(updatedPolicy);
                            // We need to refreshAll because otherwise the grid does not see the new row.
                            grid.getDataProvider().refreshAll();
                            // If we're here then this has to be the first policy in the grid and so we can select it.
                            grid.select(updatedPolicy);
                        });

        // BUG -> If you search/filter after an update the grid uses the old value in the row.
        // TODO -> Re-select same policy
        // TODO -> refilter according to the search box.
    }

    private void replacePolicy(Policy updatedPolicy) {
        experiment.getPolicies().stream()
                .filter(policy -> policy.getId() == updatedPolicy.getId())
                .forEach(policy -> {
                    policy.setExternalId(updatedPolicy.getExternalId());
                    policy.setProgress(updatedPolicy.getProgress());
                    policy.setScores(updatedPolicy.getScores());
                    policy.setRun(updatedPolicy.getRun());
                    policy.setStartedAt(updatedPolicy.getStartedAt());
                    policy.setStoppedAt(updatedPolicy.getStoppedAt());
                });
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void update(Experiment experiment, long defaultSelectedPolicyId) {
        this.experiment = experiment;

        grid.setDataProvider(new ListDataProvider<Policy>(experiment.getPolicies()));

        if (!experiment.getPolicies().isEmpty() && defaultSelectedPolicyId < 0) {
            grid.select(experiment.getPolicies().get(0));
        } else {
            experiment.getPolicies().stream()
                    .filter(policy -> policy.getId() == defaultSelectedPolicyId)
                    .findAny()
                    .ifPresent(policy -> grid.select(policy));
        }

        subscribeToEventBus(UI.getCurrent(), consumer);
    }
    
    public void selectPolicyWithId(String policyId) {
    	experiment.getPolicies().stream()
        	.filter(policy -> Long.toString(policy.getId()).equals(policyId))
        	.findAny()
        	.ifPresent(
                policy -> {
                	grid.select(policy);
                });
    }
}
