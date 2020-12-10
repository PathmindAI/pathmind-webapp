package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

public class NewExperimentViewExperimentSwitchedViewSubscriber extends ExperimentSwitchedViewSubscriber {

    private NewExperimentView newExperimentView;

    public NewExperimentViewExperimentSwitchedViewSubscriber(NewExperimentView newExperimentView) {
        this.newExperimentView = newExperimentView;
    }

    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {
        newExperimentView.setExperiment(event.getExperiment());
    }

    @Override
    public boolean filterBusEvent(ExperimentSwitchedViewBusEvent event) {
        // We only need to load the experiment if the experiment we're switching to is a draft experiment.
        return event.getExperiment().isDraft();
    }
}