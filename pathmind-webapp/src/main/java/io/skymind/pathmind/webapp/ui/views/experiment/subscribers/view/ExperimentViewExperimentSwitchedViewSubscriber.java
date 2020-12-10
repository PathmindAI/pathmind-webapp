package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class ExperimentViewExperimentSwitchedViewSubscriber extends ExperimentSwitchedViewSubscriber {

    private ExperimentView experimentView;

    public ExperimentViewExperimentSwitchedViewSubscriber(ExperimentView experimentView) {
        super();
        this.experimentView = experimentView;
    }

    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {
        experimentView.setExperiment(event.getExperiment());
    }

    @Override
    public boolean filterBusEvent(ExperimentSwitchedViewBusEvent event) {
        // We only need to load the experiment if the experiment we're switching to is NOT a draft experiment.
        return !event.getExperiment().isDraft();
    }
}