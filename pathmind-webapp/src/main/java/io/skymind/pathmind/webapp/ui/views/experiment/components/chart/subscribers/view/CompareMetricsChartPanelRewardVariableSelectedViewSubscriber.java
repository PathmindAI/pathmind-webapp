package io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.RewardVariableSelectedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.RewardVariableSelectedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.CompareMetricsChartPanel;

public class CompareMetricsChartPanelRewardVariableSelectedViewSubscriber extends RewardVariableSelectedViewSubscriber {

    private CompareMetricsChartPanel compareMetricsChartPanel;

    public CompareMetricsChartPanelRewardVariableSelectedViewSubscriber(CompareMetricsChartPanel compareMetricsChartPanel) {
        super();
        this.compareMetricsChartPanel = compareMetricsChartPanel;
    }

    @Override
    public void handleBusEvent(RewardVariableSelectedViewBusEvent event) {
        if (event.isShow()) {
            compareMetricsChartPanel.getRewardVariableFilters().putIfAbsent(event.getRewardVariable().getId(), event.getRewardVariable());
        } else {
            compareMetricsChartPanel.getRewardVariableFilters().remove(event.getRewardVariable().getId());
        }

        compareMetricsChartPanel.updateChart();
    }
}