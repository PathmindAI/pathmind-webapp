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

    @Override
    public void handleBusEvent(ExperimentRewardVariableSelectedViewBusEvent event) {
        // TODO -> STEPH -> Should this be an event or an action? Most likely an action.
        // TODO -> STEPH -> What exactly does this do again? And can this be pushed to later?
        // TODO: This is triggered upon switching between experiments. 
        // The compare metric chart is not showing the correct number of lines if user has toggled reward variables selection.
        if (event.isShow()) {
            compareMetricsChartPanel.getRewardVariableFilters().putIfAbsent(event.getRewardVariable().getId(), event.getRewardVariable());
        } else {
            compareMetricsChartPanel.getRewardVariableFilters().remove(event.getRewardVariable().getId());
        }

        compareMetricsChartPanel.updateChart();
    }
}