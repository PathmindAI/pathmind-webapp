package io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers;

import io.skymind.pathmind.webapp.bus.events.view.RewardVariableSelectedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.RewardVariableSelectedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.AllMetricsChartPanel;

public class AllMetricsChartPanelRewardVariableSelectedViewSubscriber extends RewardVariableSelectedViewSubscriber {

    private AllMetricsChartPanel allMetricsChartPanel;

    public AllMetricsChartPanelRewardVariableSelectedViewSubscriber(AllMetricsChartPanel allMetricsChartPanel) {
        super();
        this.allMetricsChartPanel = allMetricsChartPanel;
    }

    @Override
    public void handleBusEvent(RewardVariableSelectedViewBusEvent event) {
        if (event.isShow()) {
            allMetricsChartPanel.getRewardVariableFilters().putIfAbsent(event.getRewardVariable().getId(), event.getRewardVariable());
        } else {
            allMetricsChartPanel.getRewardVariableFilters().remove(event.getRewardVariable().getId());
        }

        allMetricsChartPanel.updateChart();
    }
}