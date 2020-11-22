package io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.SimulationMetricsPanel;

public class SimulationMetricsPanelExperimentSwitchedViewSubscriber extends ExperimentSwitchedViewSubscriber {

    private SimulationMetricsPanel simulationMetricsPanel;

    public SimulationMetricsPanelExperimentSwitchedViewSubscriber(SimulationMetricsPanel simulationMetricsPanel) {
        super();
        this.simulationMetricsPanel = simulationMetricsPanel;
    }

    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {
        simulationMetricsPanel.setExperiment(event.getExperiment());
    }
}
