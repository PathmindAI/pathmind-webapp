package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main;

import io.skymind.pathmind.webapp.bus.events.main.ExperimentCreatedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentCreatedSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class ExperimentViewExperimentCreatedSubscriber extends ExperimentCreatedSubscriber {

    private ExperimentView experimentView;

    public ExperimentViewExperimentCreatedSubscriber(ExperimentView experimentView) {
        super();
        this.experimentView = experimentView;
    }

    @Override
    public void handleBusEvent(ExperimentCreatedBusEvent event) {
        experimentView.updateExperimentComponentsForSubscribers();
    }

    @Override
    public boolean filterBusEvent(ExperimentCreatedBusEvent event) {
        return ExperimentUtils.isNewExperimentForModel(event.getExperiment(), experimentView.getExperiments(), experimentView.getModelId());
    }

}