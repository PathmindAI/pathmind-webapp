package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.main;

import io.skymind.pathmind.webapp.bus.events.main.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.main.ExperimentUpdatedSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class ExperimentViewExperimentUpdatedSubscriber extends ExperimentUpdatedSubscriber {

    private ExperimentView experimentView;

    public ExperimentViewExperimentUpdatedSubscriber(ExperimentView experimentView) {
        super();
        this.experimentView = experimentView;
    }

    @Override
    public void handleBusEvent(ExperimentUpdatedBusEvent event) {
        experimentView.updateExperimentComponentsForSubscribers();
    }

    @Override
    public boolean filterBusEvent(ExperimentUpdatedBusEvent event) {
        return ExperimentUtils.isSameExperiment(event.getExperiment(), experimentView.getExperiment());
    }

}