package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.experiment;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.shared.utils.PolicyUtils;
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

        Experiment experiment = experimentView.getExperiment();

        synchronized (experimentView.getExperimentLock()) {
            // TODO -> STEPH -> IS still necessary? -> Need a check in case the experiment was on hold waiting for the change of experiment to load
            if (experiment.getId() != event.getExperimentId()) {
                return;
            }

            // TODO -> STEPH -> Combined this code to reload everything in one place until we can confirm exactly what needs to be re-calculated for both
            // update runs and update policies.
            // TODO -> STEPH -> Are we going to be reloading everything? Replace the existing experiment? Etc... Who calls this event. My thinking is that the experiment
            // should already be fully loaded sow e can just update as needed. That's of course assuming that the policy has the full data required (confirm with ExperimentDAO).
            // TODO -> STEPH -> This should all be done in one place with the main susbcriber on the view and it updates all the components through the view
            // with view.setExperiment() which propagates.
            ExperimentUtils.addOrUpdatePolicies(experimentView.getExperiment(), event.getPolicies());
            // This is needed for other subscriber updates that rely on the best policy being updated.
            ExperimentUtils.updateBestPolicy(experimentView.getExperiment());

            // TODO -> STEPH -> I'm not sure which is needed and which isn't needed for the run and policy updaters...
            if(experiment.getBestPolicy() != null) {
                PolicyUtils.updateSimulationMetricsData(experiment.getBestPolicy());
                PolicyUtils.updateCompareMetricsChartData(experiment.getBestPolicy());
            }

            experiment.updateTrainingStatus();

            // TODO -> STEPH -> Do these need to be calculated and if so then do we need database calls?
//        updateTrainingErrorAndMessage(ctx, experiment);
//        ExperimentUtils.updateEarlyStopReason(experiment);

            // REFACTOR -> This method is currently overloaded and does too much but that is for another PR.
            experimentView.updateDetailsForExperiment();
        }
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return experimentView.getExperimentId() == event.getExperimentId();
    }
}