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

    @Override
    public void handleBusEvent(PolicyUpdateBusEvent event) {

        // TODO -> STEPH -> do we need experimentLock here?
        Experiment experiment = experimentView.getExperiment();

        synchronized (experimentView.getExperimentLock()) {
            // TODO -> STEPH -> I'm not sure which is needed and which isn't needed for the run and policy updaters...
            ExperimentUtils.addOrUpdatePolicies(experiment, event.getPolicies());
            ExperimentUtils.updateExperimentInternals(experiment);

            // TODO -> STEPH -> Do these need to be calculated and if so then do we need database calls?
//        updateTrainingErrorAndMessage(ctx, experiment);
//        ExperimentUtils.updateEarlyStopReason(experiment);

            experimentView.updateDetailsForExperiment();
        }
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return experimentView.getExperimentId() == event.getExperimentId();
    }
}