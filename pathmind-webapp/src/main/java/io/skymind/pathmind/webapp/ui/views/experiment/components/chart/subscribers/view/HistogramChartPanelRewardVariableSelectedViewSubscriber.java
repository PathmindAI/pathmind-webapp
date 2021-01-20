package io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.RewardVariableSelectedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.RewardVariableSelectedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.HistogramChartPanel;

public class HistogramChartPanelRewardVariableSelectedViewSubscriber extends RewardVariableSelectedViewSubscriber {
    private HistogramChartPanel histogramChartPanel;

    public HistogramChartPanelRewardVariableSelectedViewSubscriber(HistogramChartPanel histogramChartPanel) {
        super();
        this.histogramChartPanel = histogramChartPanel;
    }

    @Override
    public void handleBusEvent(RewardVariableSelectedViewBusEvent event) {
        if (event.isShow()) {
            this.histogramChartPanel.getRewardVariableFilters().putIfAbsent(event.getRewardVariable().getId(), event.getRewardVariable());
        } else {
            this.histogramChartPanel.getRewardVariableFilters().remove(event.getRewardVariable().getId());
        }

        this.histogramChartPanel.updateChart();
    }
}
