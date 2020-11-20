package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main;

import io.skymind.pathmind.webapp.bus.events.main.ExperimentCreatedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentCreatedSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

public class NewExperimentViewExperimentCreatedSubscriber extends ExperimentCreatedSubscriber {

    private NewExperimentView newExperimentView;

    public NewExperimentViewExperimentCreatedSubscriber(NewExperimentView newExperimentView) {
        this.newExperimentView = newExperimentView;
    }

    @Override
    public void handleBusEvent(ExperimentCreatedBusEvent event) {
        if (ExperimentUtils.isNewExperimentForModel(event.getExperiment(), newExperimentView.getExperiments(), newExperimentView.getModelId())) {
            newExperimentView.updateExperimentComponents();
        }
    }
}
