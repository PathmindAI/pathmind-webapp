package io.skymind.pathmind.ui.views.experiment.components;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.skymind.pathmind.bus.EventBus;
import io.skymind.pathmind.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.bus.subscribers.PolicyUpdateSubscriber;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.ui.renderer.ZonedDateTimeRenderer;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.utils.DateAndTimeUtils;

@SpringComponent
@UIScope
public class TrainingsListPanel extends VerticalLayout implements PolicyUpdateSubscriber {
    private Grid<Policy> grid;

    private Experiment experiment;

    public TrainingsListPanel() {
        setupGrid();
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
                .setResizable(true)
                .setSortable(true);

        Grid.Column<Policy> startedColumn = grid.addColumn(new ZonedDateTimeRenderer<>(Policy::getStartedAt, DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER))
                .setComparator(Comparator.comparing(Policy::getStartedAt, Comparator.nullsFirst(Comparator.naturalOrder())))
                .setHeader("Started")
                .setAutoWidth(true)
                .setResizable(true)
                .setSortable(true);

        grid.addColumn(new ZonedDateTimeRenderer<>(policy -> PolicyUtils.getRunCompletedTime(policy), DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER))
                .setComparator(Comparator.comparing(policy -> PolicyUtils.getRunCompletedTime(policy)))
                .setHeader("Completed")
                .setAutoWidth(true)
                .setResizable(true)
                .setSortable(true);

        Grid.Column<Policy> scoreColumn = grid.addColumn(policy -> PolicyUtils.getFormattedLastScore(policy))
        		.setComparator(Comparator.comparing(policy -> PolicyUtils.getLastScore(policy), Comparator.nullsLast(Comparator.naturalOrder())))
                .setHeader("Score")
                .setAutoWidth(true)
                .setTextAlign(ColumnTextAlign.END)
                .setResizable(true)
                .setSortable(true);

        grid.addColumn(policy -> PolicyUtils.getParsedPolicyName(policy))
                .setHeader("Policy")
                .setAutoWidth(true)
                .setResizable(true)
                .setSortable(true);

        grid.addColumn(policy -> policy.getRun().getRunTypeEnum())
                .setHeader("Run Type")
                .setAutoWidth(true)
                .setResizable(true)
                .setSortable(true);

        grid.addColumn(policy -> policy.getAlgorithmEnum())
                .setHeader("Algorithm")
                .setAutoWidth(true)
                .setResizable(true)
                .setSortable(true);

        grid.addColumn(Policy::getNotes)
                .setHeader("Notes")
                .setAutoWidth(true)
                .setResizable(true)
                .setSortable(true);

        grid.sort(Arrays.asList(
                new GridSortOrder<Policy>(scoreColumn, SortDirection.DESCENDING)));
    }

    public void addSelectionListener(Consumer<Policy> consumer) {
        grid.addSelectionListener(selectionPolicy ->
                selectionPolicy.getFirstSelectedItem().ifPresent(p -> consumer.accept(p)));
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
                    policy.setScores(updatedPolicy.getScores());
                    policy.setRun(updatedPolicy.getRun());
                    policy.setStartedAt(updatedPolicy.getStartedAt());
                    policy.setStoppedAt(updatedPolicy.getStoppedAt());
                });
    }

    public void init(Experiment experiment, long defaultSelectedPolicyId) {
        this.experiment = experiment;

        grid.setDataProvider(new ListDataProvider<>(experiment.getPolicies()));
        DateAndTimeUtils.refreshAfterRetrivingTimezone(UI.getCurrent(), grid.getDataProvider());

        if (!experiment.getPolicies().isEmpty() && defaultSelectedPolicyId < 0) {
            grid.select(experiment.getPolicies().get(0));
        } else {
            experiment.getPolicies().stream()
                    .filter(policy -> policy.getId() == defaultSelectedPolicyId)
                    .findAny()
                    .ifPresent(policy -> grid.select(policy));
        }
    }

    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(this);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(this);
    }

    @Override
    public void handleBusEvent(PolicyUpdateBusEvent event) {
        PushUtils.push(this, () -> updatedGrid(event.getPolicy()));
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return experiment.getId() == event.getPolicy().getExperiment().getId();
    }

    public void selectPolicyWithId(String policyId) {
    	experiment.getPolicies().stream()
        	.filter(policy -> Long.toString(policy.getId()).equals(policyId))
        	.findAny()
        	.ifPresent(policy -> grid.select(policy));
    }
}
