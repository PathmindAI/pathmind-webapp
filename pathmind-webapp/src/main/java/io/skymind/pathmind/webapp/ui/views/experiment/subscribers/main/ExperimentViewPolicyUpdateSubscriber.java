package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main;

import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class ExperimentViewPolicyUpdateSubscriber extends PolicyUpdateSubscriber {
    private ExperimentView experimentView;

    public ExperimentViewPolicyUpdateSubscriber(ExperimentView experimentView) {
        super();
        this.experimentView = experimentView;
    }

    @Override
    public void handleBusEvent(PolicyUpdateBusEvent event) {
        // Update or insert the policy in experiment.getPolicies
        ExperimentUtils.addOrUpdatePolicies(experimentView.getExperiment(), event.getPolicies());
        // This is needed for other subscriber updates that rely on the best policy being updated.
        experimentView.setBestPolicy(PolicyUtils.selectBestPolicy(experimentView.getExperiment().getPolicies()).orElse(null));
        // REFACTOR -> This method is currently overloaded and does too much but that is for another PR.
        experimentView.updateDetailsForExperiment();
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return experimentView.getExperimentId() == event.getExperimentId();
    }
}