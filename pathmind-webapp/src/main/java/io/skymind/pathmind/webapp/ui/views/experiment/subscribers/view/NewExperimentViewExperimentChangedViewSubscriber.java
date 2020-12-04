package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
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

    @Override
    public boolean filterBusEvent(ExperimentChangedViewBusEvent event) {
        return ExperimentUtils.isSameExperiment(event.getExperiment(), newExperimentView.getExperiment());
    }
}