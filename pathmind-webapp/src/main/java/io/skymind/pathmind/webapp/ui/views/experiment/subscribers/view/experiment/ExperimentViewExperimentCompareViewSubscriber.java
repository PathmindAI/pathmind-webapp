package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view.experiment;

import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentCompareViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.experiment.ExperimentCompareViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class ExperimentViewExperimentCompareViewSubscriber extends ExperimentCompareViewSubscriber {

    private ExperimentView experimentView;

    public ExperimentViewExperimentCompareViewSubscriber(ExperimentView experimentView) {
        super();
        this.experimentView = experimentView;
    }

    @Override
    public void handleBusEvent(ExperimentCompareViewBusEvent event) {
        if(event.isCompare()) {
            experimentView.startCompareExperiment(event.getExperiment());
        } else {
            experimentView.stopCompareExperiment();
        }
    }
}