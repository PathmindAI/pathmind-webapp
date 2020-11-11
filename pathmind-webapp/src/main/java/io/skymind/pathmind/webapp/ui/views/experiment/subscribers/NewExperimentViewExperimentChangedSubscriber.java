package io.skymind.pathmind.webapp.ui.views.experiment.subscribers;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

public class NewExperimentViewExperimentChangedSubscriber  extends ExperimentChangedViewSubscriber {

    private NewExperimentView newExperimentView;

    public NewExperimentViewExperimentChangedSubscriber(NewExperimentView newExperimentView) {
        this.newExperimentView = newExperimentView;
    }

    @Override
    public void handleBusEvent(ExperimentChangedViewBusEvent event) {
        if (ExperimentUtils.isSameModel(newExperimentView.getExperiment(), event.getExperiment().getModelId())) {
            newExperimentView.setExperiment(event.getExperiment());
        }
    }

    @Override
    public boolean filterBusEvent(ExperimentChangedViewBusEvent event) {
        if (newExperimentView.getExperiment() == null) {
            return false;
        }
        return ExperimentUtils.isSameModel(newExperimentView.getExperiment(), event.getExperiment().getModelId()) &&
                !ExperimentUtils.isSameExperiment(event.getExperiment(), newExperimentView.getExperiment());

    }
}
