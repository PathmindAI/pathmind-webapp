package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.subscribers.main;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.ExperimentsNavBarItem;

public class NavBarItemPolicyUpdateSubscriber extends PolicyUpdateSubscriber {

    private ExperimentsNavBarItem experimentsNavBarItem;
    private ExperimentDAO experimentDAO;

    public NavBarItemPolicyUpdateSubscriber(ExperimentsNavBarItem experimentsNavBarItem, ExperimentDAO experimentDAO) {
        super();
        this.experimentsNavBarItem = experimentsNavBarItem;
        this.experimentDAO = experimentDAO;
    }

    @Override
    public void handleBusEvent(PolicyUpdateBusEvent event) {
        updateExperimentInternalValues(event, experimentsNavBarItem.getExperiment());
        experimentsNavBarItem.updateVariableComponentValues();
    }

    private void updateExperimentInternalValues(PolicyUpdateBusEvent event, Experiment experiment) {
        // TODO -> STEPH -> This should all be done in a single ExperimentUtils method as it will have to be replicated
        //  elsewhere. This is still done this way because the trainingErrorMessage needs to be done after the update.
        ExperimentUtils.addOrUpdatePolicies(experiment, event.getPolicies());
        ExperimentUtils.updateExperimentInternals(experiment);
        experimentDAO.updateTrainingErrorAndMessage(experiment);
        ExperimentUtils.updateEarlyStopReason(experiment);
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return ExperimentUtils.isSameExperiment(experimentsNavBarItem.getExperiment(), event.getExperiment());
    }
}