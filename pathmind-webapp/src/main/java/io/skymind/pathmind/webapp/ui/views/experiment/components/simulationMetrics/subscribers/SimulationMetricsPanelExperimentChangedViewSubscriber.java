package io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.subscribers;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.SimulationMetricsPanel;

public class SimulationMetricsPanelExperimentChangedViewSubscriber extends ExperimentChangedViewSubscriber {

    private SimulationMetricsPanel simulationMetricsPanel;

    public SimulationMetricsPanelExperimentChangedViewSubscriber(SimulationMetricsPanel simulationMetricsPanel) {
        super();
        this.simulationMetricsPanel = simulationMetricsPanel;
    }

    @Override
    public void handleBusEvent(ExperimentChangedViewBusEvent event) {
        simulationMetricsPanel.setExperiment(event.getExperiment());
    }
}
