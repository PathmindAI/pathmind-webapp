package io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.utils.PathmindNumberUtils;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.ui.components.rewardVariables.RewardVariablesTable;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import io.skymind.pathmind.webapp.ui.views.experiment.components.SimulationMetricsInfoLink;
import io.skymind.pathmind.webapp.ui.views.experiment.components.SparklineChart;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimulationMetricsPanel extends HorizontalLayout implements ExperimentComponent {

    private VerticalLayout metricsWrapper;
    private VerticalLayout sparklinesWrapper;

    private RewardVariablesTable rewardVariablesTable;

    private Experiment experiment;
    private List<Span> metricSpans = new ArrayList<>();
    private List<SparklineChart> sparklineCharts = new ArrayList<>();

    private boolean showSimulationMetrics;

    public SimulationMetricsPanel(boolean showSimulationMetrics, ExperimentView experimentView) {

        super();

        this.showSimulationMetrics = showSimulationMetrics;

        setSpacing(false);
        addClassName("simulation-metrics-table-wrapper");

        // TODO -> STEPH -> Why do we have a RewardVariablesTable here in addition to the view? As in it should be a separate
        // component in the view or a subcomponent of this component. I ask because it's done differently between NewExperimentView
        // and ExperimentView. I ask because I'm trying to be consistent and I'm not sure if there are side effects, especially
        // when setting the experiment, etc.
        rewardVariablesTable = new RewardVariablesTable(experimentView);
        rewardVariablesTable.setCodeEditorMode();
        rewardVariablesTable.setCompactMode();
        rewardVariablesTable.setSelectMode();
        rewardVariablesTable.setSizeFull();

        add(rewardVariablesTable);
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

        IntStream.range(0, experiment.getRewardVariables().size())
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

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;

        // If it hasn't been rendered yet then render the simulation metrics components as they are dependent on the rewardvariables of the experiment.
        if (showSimulationMetrics && metricsWrapper == null) {
            createSimulationMetricsSpansAndSparklines();
            add(metricsWrapper, sparklinesWrapper);
        }

        // TODO -> REFACTOR -> Why are we resetting the reward variables here? Why are there are two RewardVariableTables?
        rewardVariablesTable.setExperiment(experiment);
        updateSimulationMetrics();
    }

    public void updateSimulationMetrics() {

        Policy bestPolicy = experiment.getBestPolicy();

        if (bestPolicy == null || bestPolicy.getSimulationMetrics() == null || bestPolicy.getSimulationMetrics().isEmpty()) {
            showMetricValuesAndSparklines(false);
            return;
        }

        IntStream.range(0, bestPolicy.getSimulationMetrics().size())
                .forEach(index -> {
                    Map<Integer, Double> sparklineData = bestPolicy.getSparklinesData().get(index);
                    RewardVariable rewardVariable = experiment.getRewardVariables().get(index);

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
}
