package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view.experiment;

import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.experiment.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
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
}