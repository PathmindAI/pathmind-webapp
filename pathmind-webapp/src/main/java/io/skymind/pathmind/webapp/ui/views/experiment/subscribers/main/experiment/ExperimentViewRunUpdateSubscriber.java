package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.experiment;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExperimentViewRunUpdateSubscriber extends RunUpdateSubscriber {

    private ExperimentView experimentView;
    private ExperimentDAO experimentDAO;

    public ExperimentViewRunUpdateSubscriber(ExperimentView experimentView, ExperimentDAO experimentDAO) {
        super();
        this.experimentView = experimentView;
        this.experimentDAO = experimentDAO;
    }

    @Override
    public void handleBusEvent(RunUpdateBusEvent event) {
        synchronized (experimentView.getExperimentLock()) {
            // TODO -> STEPH -> We should have a different lock for the comparison experiment.
            if(ExperimentUtils.isSameExperiment(experimentView.getExperiment(), event.getExperiment())) {
                updateExperimentInternalValues(event, experimentView.getExperiment());
                experimentView.updateComponents();
            } else {
                updateExperimentInternalValues(event, experimentView.getComparisonExperiment());
                experimentView.updateComparisonComponents();
            }
        }
    }

    private void updateExperimentInternalValues(RunUpdateBusEvent event, Experiment experiment) {
        experiment.setTrainingStatusEnum(event.getExperiment().getTrainingStatusEnum());
        ExperimentUtils.addOrUpdateRuns(experiment, event.getRuns());
        ExperimentUtils.updatedRunsForPolicies(experiment, event.getRuns());
        ExperimentUtils.updateExperimentInternals(experiment);
        experimentDAO.updateTrainingErrorAndMessage(experiment);
        ExperimentUtils.updateEarlyStopReason(experiment);
    }

    @Override
    public boolean filterBusEvent(RunUpdateBusEvent event) {
        return ExperimentUtils.isSameExperiment(experimentView.getExperiment(), event.getExperiment()) ||
                ExperimentUtils.isSameExperiment(experimentView.getComparisonExperiment(), event.getExperiment());
    }
}