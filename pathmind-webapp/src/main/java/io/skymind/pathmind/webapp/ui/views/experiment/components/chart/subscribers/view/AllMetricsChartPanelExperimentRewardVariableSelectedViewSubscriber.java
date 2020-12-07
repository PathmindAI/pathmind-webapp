package io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentRewardVariableSelectedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.experiment.ExperimentRewardVariableSelectedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.AllMetricsChartPanel;

public class AllMetricsChartPanelExperimentRewardVariableSelectedViewSubscriber extends ExperimentRewardVariableSelectedViewSubscriber {

    private AllMetricsChartPanel allMetricsChartPanel;

    public AllMetricsChartPanelExperimentRewardVariableSelectedViewSubscriber(AllMetricsChartPanel allMetricsChartPanel) {
        super();
        this.allMetricsChartPanel = allMetricsChartPanel;
    }

    @Override
    public void handleBusEvent(ExperimentRewardVariableSelectedViewBusEvent event) {
        if (event.isShow()) {
            allMetricsChartPanel.getRewardVariableFilters().putIfAbsent(event.getRewardVariable().getId(), event.getRewardVariable());
        } else {
            allMetricsChartPanel.getRewardVariableFilters().remove(event.getRewardVariable().getId());
        }

        allMetricsChartPanel.updateChart();
    }
}