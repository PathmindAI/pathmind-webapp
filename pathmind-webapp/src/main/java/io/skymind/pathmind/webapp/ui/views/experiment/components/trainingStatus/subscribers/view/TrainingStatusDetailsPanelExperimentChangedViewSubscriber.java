package io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.TrainingStatusDetailsPanel;

public class TrainingStatusDetailsPanelExperimentChangedViewSubscriber extends ExperimentChangedViewSubscriber {

    private TrainingStatusDetailsPanel trainingStatusDetailsPanel;

    public TrainingStatusDetailsPanelExperimentChangedViewSubscriber(TrainingStatusDetailsPanel trainingStatusDetailsPanel) {
        super();
        this.trainingStatusDetailsPanel = trainingStatusDetailsPanel;
    }

    @Override
    public void handleBusEvent(ExperimentChangedViewBusEvent event) {
        trainingStatusDetailsPanel.setExperiment(event.getExperiment());
    }
}
