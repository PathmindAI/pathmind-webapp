package io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.subscribers.main;

import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.MetricChartPanel;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.SimulationMetricsPanel;

import java.util.Map;

public class PopupSimulationMetricChartPanelPolicyUpdateSubscriber extends PolicyUpdateSubscriber {

    private SimulationMetricsPanel simulationMetricsPanel;
    private MetricChartPanel metricChartPanel;

    public PopupSimulationMetricChartPanelPolicyUpdateSubscriber(SimulationMetricsPanel simulationMetricsPanel, MetricChartPanel metricChartPanel) {
        this.simulationMetricsPanel = simulationMetricsPanel;
        this.metricChartPanel = metricChartPanel;
    }

    // Refactor -> This should be cleaned up along with the reward variable in MetricChartPanel. To avoid too big a refactoring
    // here I've pushed this to the separate ticket of: https://github.com/SkymindIO/pathmind-webapp/issues/2327 In other words
    // I'm concerned the refactoring could increase in scope so I've pushed it until after the feature is completed.
    @Override
    public void handleBusEvent(PolicyUpdateBusEvent event) {
        ExperimentUtils.addOrUpdatePolicies(simulationMetricsPanel.getExperiment(), event.getPolicies());
        Policy bestPolicy = PolicyUtils.selectBestPolicy(simulationMetricsPanel.getExperiment().getPolicies()).orElse(null);
        if(bestPolicy == null)
            return;
        Boolean reachedGoal = PolicyUtils.isGoalReached(metricChartPanel.getRewardVariable(), bestPolicy);
        Map<Integer, Double> sparklineData = bestPolicy.getSparklinesData().get(simulationMetricsPanel.getSparklineIndexClicked());
        // Doing it this way to let future developers know that we're reusing the reward variable because overloading
        // the method in metricChartPanel could cause someone to ignore including the reward variable as a parameter
        metricChartPanel.setGoals(metricChartPanel.getRewardVariable(), reachedGoal);
        metricChartPanel.setupChart(sparklineData, metricChartPanel.getRewardVariable());
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return simulationMetricsPanel.getExperiment().getId() == event.getExperimentId() && simulationMetricsPanel.isMetricChartPopupOpen();
    }
}
