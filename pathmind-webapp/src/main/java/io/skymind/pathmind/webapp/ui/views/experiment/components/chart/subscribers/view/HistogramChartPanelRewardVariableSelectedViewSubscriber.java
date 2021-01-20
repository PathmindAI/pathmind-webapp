package io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.RewardVariableSelectedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.RewardVariableSelectedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.HistogramChartPanel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HistogramChartPanelRewardVariableSelectedViewSubscriber extends RewardVariableSelectedViewSubscriber {
    private HistogramChartPanel histogramChartPanel;

    public HistogramChartPanelRewardVariableSelectedViewSubscriber(HistogramChartPanel histogramChartPanel) {
        super();
        this.histogramChartPanel = histogramChartPanel;
    }

    @Override
    public void handleBusEvent(RewardVariableSelectedViewBusEvent event) {
        log.info("kepricondebug hist : {}", event.getRewardVariable().getId());
        if (event.isShow()) {
            this.histogramChartPanel.getMetricsData().putIfAbsent(event.getRewardVariable().getId(), event.getRewardVariable());
        } else {
            this.histogramChartPanel.getMetricsData().remove(event.getRewardVariable().getId());
        }

        this.histogramChartPanel.updateChart();
    }
}
