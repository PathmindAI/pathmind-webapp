package io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.subscribers.main;

import io.skymind.pathmind.webapp.bus.events.main.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.TrainingStatusDetailsPanel;

public class TrainingStatusDetailsPanelRunUpdateSubscriber extends RunUpdateSubscriber {

    private TrainingStatusDetailsPanel trainingStatusDetailsPanel;

    public TrainingStatusDetailsPanelRunUpdateSubscriber(TrainingStatusDetailsPanel trainingStatusDetailsPanel) {
        super();
        this.trainingStatusDetailsPanel = trainingStatusDetailsPanel;
    }

    @Override
    public void handleBusEvent(RunUpdateBusEvent event) {
        ExperimentUtils.addOrUpdateRuns(trainingStatusDetailsPanel.getExperiment(), event.getRuns());
        trainingStatusDetailsPanel.update();
    }

    @Override
    public boolean filterBusEvent(RunUpdateBusEvent event) {
        return event.getExperimentId() == trainingStatusDetailsPanel.getExperiment().getId();
    }
}
