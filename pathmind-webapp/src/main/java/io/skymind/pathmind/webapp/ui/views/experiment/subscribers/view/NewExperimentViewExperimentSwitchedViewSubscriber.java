package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentSwitchedViewSubscriber;
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

    @Override
    public boolean filterBusEvent(ExperimentSwitchedViewBusEvent event) {
        if (newExperimentView.getExperiment() == null) {
            return false;
        }
        return ExperimentUtils.isSameModel(newExperimentView.getExperiment(), event.getExperiment().getModelId()) &&
                !ExperimentUtils.isSameExperiment(event.getExperiment(), newExperimentView.getExperiment());

    }
}
