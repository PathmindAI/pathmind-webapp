package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.experiment;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.events.main.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExperimentViewRunUpdateSubscriber extends RunUpdateSubscriber {

    private ExperimentView experimentView;

    public ExperimentViewRunUpdateSubscriber(ExperimentView experimentView) {
        super();
        this.experimentView = experimentView;
    }

    @Override
    public void handleBusEvent(RunUpdateBusEvent event) {

        // TODO -> STEPH -> do we need experimentLock here?
        Experiment experiment = experimentView.getExperiment();

        experiment.setTrainingStatusEnum(event.getExperiment().getTrainingStatusEnum());
        ExperimentUtils.addOrUpdateRuns(experiment, event.getRuns());
        ExperimentUtils.updatedRunsForPolicies(experiment, event.getRuns());
        ExperimentUtils.updateExperimentInternals(experiment);

        // TODO -> STEPH -> Do these need to be calculated and if so then do we need database calls?
//        updateTrainingErrorAndMessage(ctx, experiment);
//        ExperimentUtils.updateEarlyStopReason(experiment);

        experimentView.updateDetailsForExperiment();
    }

    @Override
    public boolean filterBusEvent(RunUpdateBusEvent event) {
        return isSameExperiment(event) || (!experimentView.getExperiment().isArchived());
    }

    private boolean isSameExperiment(RunUpdateBusEvent event) {
        return ExperimentUtils.isSameExperiment(experimentView.getExperiment(), event.getExperiment());
    }
}
