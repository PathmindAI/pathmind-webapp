package io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.subscribers.main;

import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.PolicyUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.PolicyUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.TrainingStatusDetailsPanel;

public class TrainingStatusDetailsPanelPolicyUpdateSubscriber extends PolicyUpdateSubscriber {

    private TrainingStatusDetailsPanel trainingStatusDetailsPanel;

    public TrainingStatusDetailsPanelPolicyUpdateSubscriber(TrainingStatusDetailsPanel trainingStatusDetailsPanel) {
        super();
        this.trainingStatusDetailsPanel = trainingStatusDetailsPanel;
    }

    @Override
    public void handleBusEvent(PolicyUpdateBusEvent event) {
        // TODO -> STEPH -> This should all be done in one place with the main susbcriber on the view and it updates all the components through the view
        // with view.setExperiment() which propogates.
        ExperimentUtils.addOrUpdatePolicies(trainingStatusDetailsPanel.getExperiment(), event.getPolicies());
        trainingStatusDetailsPanel.update();
    }

    @Override
    public boolean filterBusEvent(PolicyUpdateBusEvent event) {
        return trainingStatusDetailsPanel.getExperiment().getId() == event.getExperimentId();
    }
}