package io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.SimulationMetricsPanel;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
public class SimulationMetricsPolicyUpdateSubscriber extends PolicyUpdateSubscriber {

    private SimulationMetricsPanel simulationMetricsPanel;

    public SimulationMetricsPolicyUpdateSubscriber(Supplier<Optional<UI>> getUISupplier, SimulationMetricsPanel simulationMetricsPanel) {
        super(getUISupplier);
        this.simulationMetricsPanel = simulationMetricsPanel;
    }

    @Override
    public void handleBusEvent(PolicyUpdateBusEvent event) {
        PushUtils.push(getUiSupplier(), ui -> {
            ExperimentUtils.addOrUpdatePolicies(simulationMetricsPanel.getExperiment(), event.getPolicies());
            simulationMetricsPanel.updateSimulationMetrics();
        });
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return simulationMetricsPanel.getExperiment().getId() == event.getExperimentId();
    }
}