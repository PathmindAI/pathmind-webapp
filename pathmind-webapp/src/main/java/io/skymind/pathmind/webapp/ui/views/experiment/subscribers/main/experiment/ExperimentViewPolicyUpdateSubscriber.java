package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main.experiment;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class ExperimentViewPolicyUpdateSubscriber extends PolicyUpdateSubscriber {

    private ExperimentView experimentView;
    private ExperimentDAO experimentDAO;

    public ExperimentViewPolicyUpdateSubscriber(ExperimentView experimentView, ExperimentDAO experimentDAO) {
        super();
        this.experimentView = experimentView;
        this.experimentDAO = experimentDAO;
    }

    @Override
    public void handleBusEvent(PolicyUpdateBusEvent event) {
        // TODO -> STEPH -> We should have a different lock for the comparison experiment.
        if(ExperimentUtils.isSameExperiment(experimentView.getExperiment(), event.getExperiment())) {
            synchronized (experimentView.getExperimentLock()) {
                updateExperimentInternalValues(event, experimentView.getExperiment());
                experimentView.updateComponents();
            }
        } else {
            synchronized (experimentView.getComparisonExperimentLock()) {
                updateExperimentInternalValues(event, experimentView.getComparisonExperiment());
                experimentView.updateComparisonComponents();
            }
        }
    }

    private void updateExperimentInternalValues(PolicyUpdateBusEvent event, Experiment experiment) {
        ExperimentUtils.addOrUpdatePolicies(experiment, event.getPolicies());
        ExperimentUtils.updateExperimentInternals(experiment);
        experimentDAO.updateTrainingErrorAndMessage(experiment);
        ExperimentUtils.updateEarlyStopReason(experiment);
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return ExperimentUtils.isSameExperiment(experimentView.getExperiment(), event.getExperiment()) ||
                ExperimentUtils.isSameExperiment(experimentView.getComparisonExperiment(), event.getExperiment());
    }
}