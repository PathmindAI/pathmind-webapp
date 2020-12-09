package io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.experiment.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.ExperimentChartsPanel;

public class ExperimentChartsPanelExperimentSwitchedViewSubscriber extends ExperimentSwitchedViewSubscriber {

    private ExperimentChartsPanel experimentChartsPanel;

    public ExperimentChartsPanelExperimentSwitchedViewSubscriber(ExperimentChartsPanel experimentChartsPanel) {
        super();
        this.experimentChartsPanel = experimentChartsPanel;
    }

    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {
        experimentChartsPanel.setExperiment(event.getExperiment());
    }
}
