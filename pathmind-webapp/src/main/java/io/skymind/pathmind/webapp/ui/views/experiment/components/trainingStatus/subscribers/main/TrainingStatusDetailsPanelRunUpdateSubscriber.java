package io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.subscribers.main;

import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.bus.events.main.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.RunUpdateSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.components.trainingStatus.TrainingStatusDetailsPanel;

public class TrainingStatusDetailsPanelRunUpdateSubscriber extends RunUpdateSubscriber {

    private TrainingStatusDetailsPanel trainingStatusDetailsPanel;

    public TrainingStatusDetailsPanelRunUpdateSubscriber(TrainingStatusDetailsPanel trainingStatusDetailsPanel) {
        super();
        this.trainingStatusDetailsPanel = trainingStatusDetailsPanel;
    }

    @Override
    public void handleBusEvent(RunUpdateBusEvent event) {
        // TODO -> STEPH -> Are we going to be reloading everything? Replace the existing experiment? Etc... Who calls this event. My thinking is that the experiment
        // should already be fully loaded sow e can just update as needed. That's of course assuming that the policy has the full data required (confirm with ExperimentDAO).
        ExperimentUtils.addOrUpdateRuns(trainingStatusDetailsPanel.getExperiment(), event.getRuns());
        trainingStatusDetailsPanel.update();
    }

    @Override
    public boolean filterBusEvent(RunUpdateBusEvent event) {
        return event.getExperimentId() == trainingStatusDetailsPanel.getExperiment().getId();
    }
}
