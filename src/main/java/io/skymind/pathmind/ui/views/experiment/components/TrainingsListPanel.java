package io.skymind.pathmind.ui.views.experiment.components;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;

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
import io.skymind.pathmind.bus.events.RunUpdateBusEvent;
import io.skymind.pathmind.bus.subscribers.PolicyUpdateSubscriber;
import io.skymind.pathmind.bus.subscribers.RunUpdateSubscriber;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.ui.renderer.ZonedDateTimeRenderer;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.utils.DateAndTimeUtils;

@SpringComponent
@UIScope
public class TrainingsListPanel extends VerticalLayout
{
    private Grid<Policy> grid;

    private Experiment experiment;

    private TrainingListPolicyUpdateSubscriber policyUpdateSubscriber;
    private TrainingListRunUpdateSubscriber runUpdateSubscriber;

    public TrainingsListPanel() {
        setupGrid();
        add(grid);

        policyUpdateSubscriber = new TrainingListPolicyUpdateSubscriber();
        runUpdateSubscriber = new TrainingListRunUpdateSubscriber();

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

        grid.addColumn(new ZonedDateTimeRenderer<>(Policy::getStartedAt, DateAndTimeUtils.STANDARD_DATE_AND_TIME_SHORT_FOMATTER))
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

        grid.addColumn(Policy::getName)
                .setHeader("Policy")
                .setAutoWidth(true)
                .setResizable(true)
                .setSortable(true);

        grid.addColumn(policy -> policy.getRun().getRunTypeEnum())
                .setHeader("Run Type")
                .setAutoWidth(true)
                .setResizable(true)
                .setSortable(true);

        grid.addColumn(Policy::getAlgorithmEnum)
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

    private void updatedRunForPoliciesInGrid(Run run) {
        experiment.getPolicies().stream()
                .filter(policy -> policy.getRunId() == run.getId())
                .forEach(policy -> {
                    policy.setRun(run);
                    grid.getDataProvider().refreshItem(policy);
                });
        // BUG -> If you search/filter after an update the grid uses the old value in the row.
        // TODO -> Re-select same policy
        // TODO -> refilter according to the search box.
    }

    private void updatePolicyInGrid(Policy updatedPolicy) {
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
                    // TODO -> REFACTOR -> Do all the values need to be reset?
                    policy.setName(updatedPolicy.getName());
                    policy.setNotes(updatedPolicy.getNotes());
                    policy.setExternalId(updatedPolicy.getExternalId());
                    policy.setScores(updatedPolicy.getScores());
                    policy.setRun(updatedPolicy.getRun());
                    policy.setStartedAt(updatedPolicy.getStartedAt());
                    policy.setStoppedAt(updatedPolicy.getStoppedAt());
                });
    }

    public void init(Experiment experiment, long defaultSelectedPolicyId) {
        this.experiment = experiment;

        DateAndTimeUtils.withUserTimeZoneId(timeZoneId -> {
            // grid uses ZonedDateTimeRenderer, making sure here that time zone id is loaded properly before setting items
            grid.setDataProvider(new ListDataProvider<>(experiment.getPolicies()));
            if (!experiment.getPolicies().isEmpty() && defaultSelectedPolicyId < 0) {
            	grid.select(experiment.getPolicies().get(0));
            } else {
            	experiment.getPolicies().stream()
            	.filter(policy -> policy.getId() == defaultSelectedPolicyId)
            	.findAny()
            	.ifPresent(policy -> grid.select(policy));
            }
        });

    }

    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(policyUpdateSubscriber);
        EventBus.unsubscribe(runUpdateSubscriber);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(policyUpdateSubscriber);
        EventBus.subscribe(runUpdateSubscriber);
    }

    public void selectPolicyWithId(String policyId) {
        experiment.getPolicies().stream()
                .filter(policy -> Long.toString(policy.getId()).equals(policyId))
                .findAny()
                .ifPresent(policy -> grid.select(policy));
    }

    class TrainingListPolicyUpdateSubscriber implements PolicyUpdateSubscriber
    {
        @Override
        public void handleBusEvent(PolicyUpdateBusEvent event) {
            PushUtils.push(getUI(), () -> updatePolicyInGrid(event.getPolicy()));
        }

        @Override
        public boolean filterBusEvent(PolicyUpdateBusEvent event) {
            return experiment.getId() == event.getPolicy().getExperiment().getId();
        }

        @Override
        public Optional<UI> getUI() {
            return TrainingsListPanel.this.getUI();
        }
    }

    class TrainingListRunUpdateSubscriber implements RunUpdateSubscriber
    {
        @Override
        public boolean filterBusEvent(RunUpdateBusEvent event) {
            return experiment.getId() == event.getRun().getExperiment().getId();
        }

        @Override
        public void handleBusEvent(RunUpdateBusEvent event) {
            PushUtils.push(getUI(), () -> updatedRunForPoliciesInGrid(event.getRun()));
        }

        @Override
        public Optional<UI> getUI() {
            return TrainingsListPanel.this.getUI();
        }
    }
}
