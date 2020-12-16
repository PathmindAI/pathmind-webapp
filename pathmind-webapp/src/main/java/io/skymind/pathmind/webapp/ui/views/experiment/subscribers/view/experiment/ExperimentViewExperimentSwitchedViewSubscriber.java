package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view.experiment;

import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.experiment.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

public class ExperimentViewExperimentSwitchedViewSubscriber extends ExperimentSwitchedViewSubscriber {

    private ExperimentView experimentView;

    public ExperimentViewExperimentSwitchedViewSubscriber(ExperimentView experimentView) {
        super();
        this.experimentView = experimentView;
    }

    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {
        // If it's a new draft experiment then we just want to go to the draft view and ignore this.
        if (event.getExperiment().isDraft()) {
            getUiSupplier().get().ifPresent(ui -> ui.navigate(NewExperimentView.class, event.getExperiment().getId()));
            return;
        }

        getUiSupplier().get().ifPresent(ui -> ui.getPage().getHistory().pushState(null, Routes.EXPERIMENT_URL + "/" + event.getExperiment().getId()));
        experimentView.setExperiment(event.getExperiment());
    }
}