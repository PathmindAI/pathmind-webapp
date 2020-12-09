package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view.newExperiment;

import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.experiment.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

public class NewExperimentViewExperimentSwitchedViewSubscriber extends ExperimentSwitchedViewSubscriber {

    private NewExperimentView newExperimentView;

    public NewExperimentViewExperimentSwitchedViewSubscriber(NewExperimentView newExperimentView) {
        this.newExperimentView = newExperimentView;
    }

    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {
        if (ExperimentUtils.isSameModel(newExperimentView.getExperiment(), event.getExperiment().getModelId())) {
            newExperimentView.setExperiment(event.getExperiment());
        }
    }
}
