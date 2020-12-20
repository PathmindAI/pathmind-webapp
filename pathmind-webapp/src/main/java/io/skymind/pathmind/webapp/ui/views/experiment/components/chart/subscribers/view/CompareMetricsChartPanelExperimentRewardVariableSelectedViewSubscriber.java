package io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentRewardVariableSelectedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.experiment.ExperimentRewardVariableSelectedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.CompareMetricsChartPanel;

public class CompareMetricsChartPanelExperimentRewardVariableSelectedViewSubscriber extends ExperimentRewardVariableSelectedViewSubscriber {

    private CompareMetricsChartPanel compareMetricsChartPanel;

    public CompareMetricsChartPanelExperimentRewardVariableSelectedViewSubscriber(CompareMetricsChartPanel compareMetricsChartPanel) {
        super();
        this.compareMetricsChartPanel = compareMetricsChartPanel;
    }

    /**
     * This is an event rather than an action because it's not always needed, and in those cases that it is the nested component structure is quite
     * significant and as a result it's a lot easier to manage this with an event than an action even though it should technically be an action.
     * @param event
     */
    @Override
    public void handleBusEvent(ExperimentRewardVariableSelectedViewBusEvent event) {
        if (event.isShow()) {
            compareMetricsChartPanel.getRewardVariableFilters().putIfAbsent(event.getRewardVariable().getId(), event.getRewardVariable());
        } else {
            compareMetricsChartPanel.getRewardVariableFilters().remove(event.getRewardVariable().getId());
        }

        compareMetricsChartPanel.updateChart();
    }
}