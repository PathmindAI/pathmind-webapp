package io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.experiment.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.TrainingStatusDetailsPanel;

public class TrainingStatusDetailsPanelExperimentSwitchedViewSubscriber extends ExperimentSwitchedViewSubscriber {

    private TrainingStatusDetailsPanel trainingStatusDetailsPanel;

    public TrainingStatusDetailsPanelExperimentSwitchedViewSubscriber(TrainingStatusDetailsPanel trainingStatusDetailsPanel) {
        super();
        this.trainingStatusDetailsPanel = trainingStatusDetailsPanel;
    }

    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {
        trainingStatusDetailsPanel.setExperiment(event.getExperiment());
    }
}
