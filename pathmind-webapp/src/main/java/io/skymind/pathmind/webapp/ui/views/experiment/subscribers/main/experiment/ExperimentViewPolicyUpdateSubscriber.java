package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.experiment;

import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class ExperimentViewPolicyUpdateSubscriber extends PolicyUpdateSubscriber {

    private ExperimentView experimentView;

    public ExperimentViewPolicyUpdateSubscriber(ExperimentView experimentView) {
        super();
        this.experimentView = experimentView;
    }

    // TODO -> STEPH -> Confirm that this is only needed for button enabled and error messaging.
    // .. For button enablement and error messaging more than anything else
    @Override
    public void handleBusEvent(PolicyUpdateBusEvent event) {
        // TODO -> STEPH -> do we need experimentLock here?
        synchronized (experimentView.getExperimentLock()) {
            // Need a check in case the experiment was on hold waiting for the change of experiment to load
            if (event.getExperimentId() != event.getExperimentId()) {
                return;
            }
            // TODO -> STEPH -> Are we going to be reloading everything? Replace the existing experiment? Etc... Who calls this event. My thinking is that the experiment
            // should already be fully loaded sow e can just update as needed. That's of course assuming that the policy has the full data required (confirm with ExperimentDAO).
            // TODO -> STEPH -> This should all be done in one place with the main susbcriber on the view and it updates all the components through the view
            // with view.setExperiment() which propogates.
            ExperimentUtils.addOrUpdatePolicies(experimentView.getExperiment(), event.getPolicies());
            // This is needed for other subscriber updates that rely on the best policy being updated.
            ExperimentUtils.updateBestPolicy(experimentView.getExperiment());

            // REFACTOR -> This method is currently overloaded and does too much but that is for another PR.
            experimentView.updateDetailsForExperiment();
        }
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return experimentView.getExperimentId() == event.getExperimentId();
    }
}