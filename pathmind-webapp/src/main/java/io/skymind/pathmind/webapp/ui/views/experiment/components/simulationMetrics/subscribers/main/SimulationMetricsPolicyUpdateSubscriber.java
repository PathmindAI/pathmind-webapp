package io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.subscribers.main;

import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.SimulationMetricsPanel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimulationMetricsPolicyUpdateSubscriber extends PolicyUpdateSubscriber {

    private SimulationMetricsPanel simulationMetricsPanel;

    public SimulationMetricsPolicyUpdateSubscriber(SimulationMetricsPanel simulationMetricsPanel) {
        super();
        this.simulationMetricsPanel = simulationMetricsPanel;
    }

    @Override
    public void handleBusEvent(PolicyUpdateBusEvent event) {
        // TODO -> STEPH -> This should all be done in one place with the main susbcriber on the view and it updates all the components through the view
        // with view.setExperiment() which propogates.
        ExperimentUtils.addOrUpdatePolicies(simulationMetricsPanel.getExperiment(), event.getPolicies());
        simulationMetricsPanel.updateSimulationMetrics();
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return simulationMetricsPanel.getExperiment().getId() == event.getExperimentId();
    }
}