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

        Experiment experiment = experimentView.getExperiment();

        experiment.setTrainingStatusEnum(event.getExperiment().getTrainingStatusEnum());
        // TODO -> STEPH -> Are we going to be reloading everything? Replace the existing experiment? Etc... Who calls this event. My thinking is that the experiment
        // should already be fully loaded sow e can just update as needed. That's of course assuming that the policy has the full data required (confirm with ExperimentDAO).
        ExperimentUtils.addOrUpdateRuns(experiment, event.getRuns());
        ExperimentUtils.updatedRunsForPolicies(experiment, event.getRuns());

        ExperimentUtils.updateBestPolicy(experiment);
        // TODO -> STEPH -> This one just tricked me up a lot tonight and so needs to be a bit more obvious or setup somewhere else. Switching experiment, update, etc. will NOT work without it.
        if(experiment.getBestPolicy() != null) {
            PolicyUtils.updateSimulationMetricsData(experiment.getBestPolicy());
            PolicyUtils.updateCompareMetricsChartData(experiment.getBestPolicy());
        }

        experiment.updateTrainingStatus();

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
