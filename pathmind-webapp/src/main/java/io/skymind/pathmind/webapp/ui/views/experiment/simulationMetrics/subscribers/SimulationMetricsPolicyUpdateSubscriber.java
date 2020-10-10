package io.skymind.pathmind.webapp.ui.views.experiment.simulationMetrics.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.simulationMetrics.SimulationMetricsPanel;

import java.util.Optional;
import java.util.function.Supplier;

public class SimulationMetricsPolicyUpdateSubscriber extends PolicyUpdateSubscriber {

    private SimulationMetricsPanel simulationMetricsPanel;

    public SimulationMetricsPolicyUpdateSubscriber(Supplier<Optional<UI>> getUISupplier, SimulationMetricsPanel simulationMetricsPanel) {
        super(getUISupplier);
        this.simulationMetricsPanel = simulationMetricsPanel;
    }

    @Override
    public void handleBusEvent(PolicyUpdateBusEvent event) {
        PushUtils.push(getUiSupplier(), ui -> simulationMetricsPanel.updatePolicies(event.getPolicies()));
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return simulationMetricsPanel.getExperiment().getId() == event.getExperimentId();
    }
}