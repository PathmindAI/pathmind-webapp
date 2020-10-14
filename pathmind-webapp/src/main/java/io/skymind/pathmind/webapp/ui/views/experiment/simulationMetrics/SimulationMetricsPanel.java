package io.skymind.pathmind.webapp.ui.views.experiment.simulationMetrics;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.utils.PathmindNumberUtils;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.SimulationMetricsInfoLink;
import io.skymind.pathmind.webapp.ui.views.experiment.components.SparklineChart;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.MetricChartPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.simulationMetrics.subscribers.SimulationMetricsPanelRunUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.simulationMetrics.subscribers.SimulationMetricsPolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.model.components.rewardVariables.RewardVariablesTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class SimulationMetricsPanel extends HorizontalLayout {

    private Supplier<Optional<UI>> getUISupplier;

    private MetricChartPanel metricChartPanel;
    private Dialog metricChartDialog;
    private VerticalLayout metricsWrapper;
    private VerticalLayout sparklinesWrapper;

    private RewardVariablesTable rewardVariablesTable;

    private boolean showSimulationMetrics;

    private Experiment experiment;
    private List<RewardVariable> rewardVariables;
    private List<Span> metricSpans = new ArrayList<>();
    private List<SparklineChart> sparklineCharts = new ArrayList<>();

    public SimulationMetricsPanel(Experiment experiment, boolean showSimulationMetrics, List<RewardVariable> rewardVariables, Supplier<Optional<UI>> getUISupplier) {

        super();
        this.experiment = experiment;
        this.rewardVariables= rewardVariables;
        this.showSimulationMetrics = showSimulationMetrics;
        this.getUISupplier = getUISupplier;

        setSpacing(false);
        addClassName("simulation-metrics-table-wrapper");

        createEnlargedChartDialog();

        rewardVariablesTable = new RewardVariablesTable(getUISupplier);
        rewardVariablesTable.setCodeEditorMode();
        rewardVariablesTable.setCompactMode();
        rewardVariablesTable.setSelectMode();
        rewardVariablesTable.setSizeFull();
        rewardVariablesTable.setRewardVariables(rewardVariables);

        add(rewardVariablesTable);

        if (showSimulationMetrics) {
            metricsWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
            metricsWrapper.addClassName("metrics-wrapper");
            sparklinesWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
            sparklinesWrapper.addClassName("sparklines-wrapper");

            updateSimulationMetrics(true);

            add(metricsWrapper, sparklinesWrapper);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if(experiment.isArchived())
            return;
        EventBus.subscribe(this,
                new SimulationMetricsPolicyUpdateSubscriber(getUISupplier, this),
                new SimulationMetricsPanelRunUpdateSubscriber(getUISupplier, this));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        EventBus.unsubscribe(this);
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        updateSimulationMetrics(true);
    }

    public void updatePolicies(List<Policy> updatedPolicies) {
        ExperimentUtils.addOrUpdatePolicies(experiment, updatedPolicies);
        updateSimulationMetrics(true);
    }

    public boolean isShowSimulationMetrics() {
        return showSimulationMetrics;
    }

    // TODO -> it should only be true for the first time, but this is a hotfix
    private void updateSimulationMetrics(Boolean createElementsFromScratch) {

        if (createElementsFromScratch) {
            metricsWrapper.removeAll();
            sparklinesWrapper.removeAll();
        }

        Policy bestPolicy = PolicyUtils.selectBestPolicy(experiment.getPolicies()).orElse(null);

        // Needed to convert the raw metrics to a format the UI can use.
        PolicyUtils.updateSimulationMetricsData(bestPolicy);

        if (bestPolicy == null || bestPolicy.getSimulationMetrics() == null || bestPolicy.getSimulationMetrics().isEmpty()) {
            return;
        }

        if (createElementsFromScratch) {
            Div sparklineHeader = new Div(new Span("Overview"), new SimulationMetricsInfoLink());
            sparklineHeader.addClassName("header");
            sparklinesWrapper.add(sparklineHeader);

            Div metricsHeader = new Div(new Span("Value"), new SimulationMetricsInfoLink());
            metricsHeader.addClassName("header");
            metricsWrapper.add(metricsHeader);
        }

        IntStream.range(0, bestPolicy.getSimulationMetrics().size())
                .forEach(index -> {
                    Span metricSpan = new Span();
                    Map<Integer, Double> sparklineData = bestPolicy.getSparklinesData().get(index);
                    RewardVariable rewardVariable = rewardVariables.get(index);

                    // First conditional value is with uncertainty, second value is without uncertainty
                    String metricValue = bestPolicy.getUncertainty() != null && !bestPolicy.getUncertainty().isEmpty()
                            ? bestPolicy.getUncertainty().get(index)
                            : PathmindNumberUtils.formatNumber(bestPolicy.getSimulationMetrics().get(index));

                    if (rewardVariable.getGoalConditionTypeEnum() != null){
                        Boolean reachedGoal = PolicyUtils.isGoalReached(rewardVariable, bestPolicy);
                        String metricSpanColorClass = reachedGoal ? "success-text" : "failure-text";
                        metricSpan.addClassName(metricSpanColorClass);
                    }
                    if (createElementsFromScratch) {
                        metricsWrapper.add(metricSpan);
                        metricSpans.add(metricSpan);

                        SparklineChart sparkline = new SparklineChart();
                        sparkline.setSparkLine(sparklineData, rewardVariable, false);
                        Button enlargeButton = new Button("Show");
                        enlargeButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
                        enlargeButton.addClickListener(event -> {
                            Boolean reachedGoal = PolicyUtils.isGoalReached(rewardVariable, bestPolicy);
                            metricChartPanel.setGoals(rewardVariable, reachedGoal);
                            metricChartPanel.setupChart(sparklineData, rewardVariable);
                            metricChartDialog.open();
                        });
                        VerticalLayout sparkLineWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                            sparkline,
                            enlargeButton
                        );
                        sparkLineWrapper.addClassName("sparkline");
                        sparklinesWrapper.add(sparkLineWrapper);
                        sparklineCharts.add(sparkline);
                    } else {
                        metricSpan = metricSpans.get(index);
                        sparklineCharts.get(index).setSparkLine(sparklineData, rewardVariable, false);
                    }
                    metricSpan.setText(metricValue);
                });
    }

    private void createEnlargedChartDialog() {
        metricChartPanel = new MetricChartPanel();
        Button metricChartDialogCloseButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        metricChartDialogCloseButton.addClickListener(event -> metricChartDialog.close());
        metricChartDialog = new Dialog();
        metricChartDialog.setWidth("60vw");
        metricChartDialog.add(metricChartPanel, metricChartDialogCloseButton);
    }

}
