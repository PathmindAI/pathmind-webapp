package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view;

import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class ExperimentViewExperimentChangedViewSubscriber extends ExperimentChangedViewSubscriber {

    private ExperimentView experimentView;

    public ExperimentViewExperimentChangedViewSubscriber(ExperimentView experimentView) {
        super();
        this.experimentView = experimentView;
    }

    @Override
    public void handleBusEvent(ExperimentChangedViewBusEvent event) {
        experimentView.setExperiment(event.getExperiment());
    }

    @Override
    public boolean filterBusEvent(ExperimentChangedViewBusEvent event) {
        if (experimentView.getExperiment() == null) {
            return false;
        }
        return ExperimentUtils.isSameModel(experimentView.getExperiment(), event.getExperiment().getModelId()) &&
                !ExperimentUtils.isSameExperiment(event.getExperiment(), experimentView.getExperiment());

    }
}