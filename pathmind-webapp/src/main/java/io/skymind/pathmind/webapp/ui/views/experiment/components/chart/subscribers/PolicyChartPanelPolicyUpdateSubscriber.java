package io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.utils.PushUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.PolicyChartPanel;

import java.util.Optional;
import java.util.function.Supplier;

public class PolicyChartPanelPolicyUpdateSubscriber extends PolicyUpdateSubscriber {

    private PolicyChartPanel policyChartPanel;

    public PolicyChartPanelPolicyUpdateSubscriber(Supplier<Optional<UI>> getUISupplier, PolicyChartPanel policyChartPanel) {
        super(getUISupplier);
        this.policyChartPanel = policyChartPanel;
    }

    @Override
    public void handleBusEvent(PolicyUpdateBusEvent event) {
        synchronized (policyChartPanel.getExperimentLock()) {
            // We need to check after the lock is acquired as changing experiments can take up to several seconds.
            if (event.getExperimentId() != policyChartPanel.getExperimentId())
                return;

            ExperimentUtils.addOrUpdatePolicies(policyChartPanel.getExperiment(), event.getPolicies());
            PushUtils.push(getUiSupplier(), () -> policyChartPanel.updateChart());
        }
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return policyChartPanel.getExperimentId() == event.getExperimentId();
    }

}