package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view.newExperiment;

import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentNeedsSavingViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.experiment.ExperimentChangedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

public class NewExperimentViewExperimentNeedsSavingViewSubscriber extends ExperimentChangedViewSubscriber {

    private NewExperimentView newExperimentView;

    public NewExperimentViewExperimentNeedsSavingViewSubscriber(NewExperimentView newExperimentView) {
        this.newExperimentView = newExperimentView;
    }

    /**
     * We do not need to pass the experiment object around because all the components, including the view, should
     * now all be using the same instance and so any updates to the experiment are in real time.
     */
    @Override
    public void handleBusEvent(ExperimentNeedsSavingViewBusEvent event) {
        newExperimentView.setNeedsSaving();
    }
}