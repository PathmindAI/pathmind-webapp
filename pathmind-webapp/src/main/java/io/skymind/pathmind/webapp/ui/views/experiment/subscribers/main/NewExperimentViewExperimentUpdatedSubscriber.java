package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main;

import io.skymind.pathmind.webapp.bus.events.main.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

public class NewExperimentViewExperimentUpdatedSubscriber extends ExperimentUpdatedSubscriber {

    private NewExperimentView newExperimentView;

    public NewExperimentViewExperimentUpdatedSubscriber(NewExperimentView newExperimentView) {
        this.newExperimentView = newExperimentView;
    }

    @Override
    public void handleBusEvent(ExperimentUpdatedBusEvent event) {
        newExperimentView.updateExperimentComponents();
    }

    @Override
    public boolean filterBusEvent(ExperimentUpdatedBusEvent event) {
        return ExperimentUtils.isSameExperiment(event.getExperiment(), newExperimentView.getExperiment());
    }
}
