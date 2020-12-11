package io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.ExperimentChartsPanel;

public class ExperimentChartsPanelExperimentSwitchedViewSubscriber extends ExperimentSwitchedViewSubscriber {

    private ExperimentChartsPanel experimentChartsPanel;

    public ExperimentChartsPanelExperimentSwitchedViewSubscriber(ExperimentChartsPanel experimentChartsPanel) {
        super();
        this.experimentChartsPanel = experimentChartsPanel;
    }

    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {
        experimentChartsPanel.setupCharts(event.getExperiment(), experimentChartsPanel.getRewardVariables());
    }
}
