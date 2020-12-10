package io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.db.utils.RewardVariablesUtils;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.utils.PathmindNumberUtils;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.SimulationMetricsInfoLink;
import io.skymind.pathmind.webapp.ui.views.experiment.components.SparklineChart;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.MetricChartPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.subscribers.main.PopupSimulationMetricChartPanelPolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.subscribers.view.SimulationMetricsPanelExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.subscribers.main.SimulationMetricsPolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.components.rewardVariables.RewardVariablesTable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    // REFACTOR -> A quick somewhat hacky solution until we have time to refactor the code
    private int indexClicked;

    public SimulationMetricsPanel(Experiment experiment, boolean showSimulationMetrics, List<RewardVariable> rewardVariables, Supplier<Optional<UI>> getUISupplier) {

        super();
        this.experiment = experiment.deepClone();
        this.rewardVariables = RewardVariablesUtils.deepClone(rewardVariables);
        this.showSimulationMetrics = showSimulationMetrics;
        this.getUISupplier = getUISupplier;

        setSpacing(false);
        addClassName("simulation-metrics-table-wrapper");

        rewardVariablesTable = new RewardVariablesTable(getUISupplier);
        rewardVariablesTable.setCodeEditorMode();
        rewardVariablesTable.setCompactMode();
        rewardVariablesTable.setSelectMode();
        rewardVariablesTable.setSizeFull();
        rewardVariablesTable.setRewardVariables(rewardVariables);

        add(rewardVariablesTable);

        if (showSimulationMetrics) {
            createSimulationMetricsSpansAndSparklines();

            updateSimulationMetrics();

            add(metricsWrapper, sparklinesWrapper);
        }
    }

    private void createSimulationMetricsSpansAndSparklines() {
        metricsWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        metricsWrapper.addClassName("metrics-wrapper");
        Div metricsHeader = new Div(new Span("Value"), new SimulationMetricsInfoLink());
        metricsHeader.addClassName("header");
        metricsWrapper.add(metricsHeader);

        sparklinesWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        sparklinesWrapper.addClassName("sparklines-wrapper");
        Div sparklineHeader = new Div(new Span("Overview"), new SimulationMetricsInfoLink());
        sparklineHeader.addClassName("header");
        sparklinesWrapper.add(sparklineHeader);

        Policy bestPolicy = PolicyUtils.selectBestPolicy(experiment.getPolicies()).orElse(null);

        // Needed to convert the raw metrics to a format the UI can use.
        PolicyUtils.updateSimulationMetricsData(bestPolicy);

        IntStream.range(0, rewardVariables.size())
                .forEach(index -> {
                    Span metricSpan = new Span();
                    SparklineChart sparkline = new SparklineChart();
                    metricSpans.add(metricSpan);
                    sparklineCharts.add(sparkline);

                    VerticalLayout sparkLineWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                            sparkline
                    );
                    sparkLineWrapper.addClassName("sparkline");

                    metricsWrapper.add(metricSpan);
                    sparklinesWrapper.add(sparkLineWrapper);
                });

        showMetricValuesAndSparklines(false);
    }

    private void showMetricValuesAndSparklines(Boolean show) {
        metricsWrapper.setVisible(show);
        sparklinesWrapper.setVisible(show);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (experiment.isArchived()) {
            return;
        }
        EventBus.subscribe(this, getUISupplier,
                new SimulationMetricsPolicyUpdateSubscriber(this),
                new SimulationMetricsPanelExperimentSwitchedViewSubscriber(this),
                new PopupSimulationMetricChartPanelPolicyUpdateSubscriber(this, metricChartPanel));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        EventBus.unsubscribe(this);
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment.deepClone();
        updateSimulationMetrics();
    }

    public boolean isShowSimulationMetrics() {
        return showSimulationMetrics;
    }

    public void updateSimulationMetrics() {

        Policy bestPolicy = PolicyUtils.selectBestPolicy(experiment.getPolicies()).orElse(null);

        // Needed to convert the raw metrics to a format the UI can use.
        PolicyUtils.updateSimulationMetricsData(bestPolicy);

        if (bestPolicy == null || bestPolicy.getSimulationMetrics() == null || bestPolicy.getSimulationMetrics().isEmpty()) {
            showMetricValuesAndSparklines(false);
            return;
        }

        IntStream.range(0, bestPolicy.getSimulationMetrics().size())
                .forEach(index -> {
                    Map<Integer, Double> sparklineData = bestPolicy.getSparklinesData().get(index);
                    RewardVariable rewardVariable = rewardVariables.get(index);

                    // First conditional value is with uncertainty, second value is without uncertainty
                    String metricValue = bestPolicy.getUncertainty() != null && !bestPolicy.getUncertainty().isEmpty()
                            ? bestPolicy.getUncertainty().get(index)
                            : PathmindNumberUtils.formatNumber(bestPolicy.getSimulationMetrics().get(index));

                    if (rewardVariable.getGoalConditionTypeEnum() != null) {
                        Boolean reachedGoal = PolicyUtils.isGoalReached(rewardVariable, bestPolicy);
                        String metricSpanColorClass = reachedGoal ? "success-text" : "failure-text";
                        metricSpans.get(index).addClassName(metricSpanColorClass);
                    }
                    sparklineCharts.get(index).setSparkLine(sparklineData, rewardVariable, false, index);
                    metricSpans.get(index).setText(metricValue);
                });

        showMetricValuesAndSparklines(true);
    }

    public boolean isMetricChartPopupOpen() {
        return metricChartDialog.isOpened();
    }

    public int getSparklineIndexClicked() {
        return indexClicked;
    }
}
