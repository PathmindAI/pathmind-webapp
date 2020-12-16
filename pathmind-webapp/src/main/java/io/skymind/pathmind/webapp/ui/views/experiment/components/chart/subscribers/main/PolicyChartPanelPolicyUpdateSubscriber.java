package io.skymind.pathmind.webapp.ui.views.experiment.components.chart.subscribers.main;

import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.PolicyChartPanel;

public class PolicyChartPanelPolicyUpdateSubscriber extends PolicyUpdateSubscriber {

    private PolicyChartPanel policyChartPanel;

    public PolicyChartPanelPolicyUpdateSubscriber(PolicyChartPanel policyChartPanel) {
        super();
        this.policyChartPanel = policyChartPanel;
    }

    @Override
    public void handleBusEvent(PolicyUpdateBusEvent event) {
        synchronized (policyChartPanel.getExperimentLock()) {
            // We need to check after the lock is acquired as changing experiments can take up to several seconds.
            if (event.getExperimentId() != policyChartPanel.getExperimentId()) {
                return;
            }

            // TODO -> STEPH -> This should all be done in one place with the main susbcriber on the view and it updates all the components through the view
            // with view.setExperiment() which propogates.
            ExperimentUtils.addOrUpdatePolicies(policyChartPanel.getExperiment(), event.getPolicies());
            policyChartPanel.updateChart();
        }
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return policyChartPanel.getExperimentId() == event.getExperimentId();
    }

}