package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view.newExperiment;

import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.experiment.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

public class NewExperimentViewExperimentChangedViewSubscriber extends ExperimentChangedViewSubscriber {

    private NewExperimentView newExperimentView;

    public NewExperimentViewExperimentChangedViewSubscriber(NewExperimentView newExperimentView) {
        this.newExperimentView = newExperimentView;
    }

    @Override
    public void handleBusEvent(ExperimentChangedViewBusEvent event) {
        newExperimentView.setNeedsSaving();
    }
}