package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.utils.PolicyBusEventUtils;
import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.data.*;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.services.training.progress.ProgressInterpreter;
import io.skymind.pathmind.ui.components.SearchBox;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.ui.views.policy.filter.PolicyFilter;
import io.skymind.pathmind.utils.DateAndTimeUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class TrainingsListPanel extends VerticalLayout {
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

        grid.addColumn(new LocalDateTimeRenderer<>(policy -> PolicyUtils.getRunCompletedTime(policy), DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER))
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

        grid.addColumn(policy -> ProgressInterpreter.interpretKey(policy.getName()).getAlgorithm())
                .setHeader("Algorithm")
                .setAutoWidth(true)
                .setSortable(true);

        grid.addColumn(policy -> ProgressInterpreter.interpretKey(policy.getName()).getHyperParameters().toString().replaceAll("(\\{|\\})", ""))
                .setHeader("Notes")
                .setAutoWidth(true)
                .setSortable(true);
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
                    // TODO -> https://github.com/SkymindIO/pathmind-webapp/issues/229 -> Are these the only values we need to update?
                    policy.setExternalId(updatedPolicy.getExternalId());
                    policy.setProgress(updatedPolicy.getProgress());
                    policy.setScores(updatedPolicy.getScores());
                    policy.setRun(updatedPolicy.getRun());
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
}
