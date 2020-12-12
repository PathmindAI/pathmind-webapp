package io.skymind.pathmind.webapp.ui.views.experiment.subscribers.view.newExperiment;

import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.bus.events.view.experiment.ExperimentSwitchedViewBusEvent;
import io.skymind.pathmind.webapp.bus.subscribers.view.experiment.ExperimentSwitchedViewSubscriber;
import io.skymind.pathmind.webapp.data.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

public class NewExperimentViewExperimentSwitchedViewSubscriber extends ExperimentSwitchedViewSubscriber {

    private NewExperimentView newExperimentView;

    public NewExperimentViewExperimentSwitchedViewSubscriber(NewExperimentView newExperimentView) {
        this.newExperimentView = newExperimentView;
    }

    @Override
    public void handleBusEvent(ExperimentSwitchedViewBusEvent event) {

        // First save draft before switching as part of the auto-save functionality.
        newExperimentView.saveDraftExperiment(() -> {
            // If it's a not a new draft experiment then we just want to go to the experiment view and ignore this.
            if (!ExperimentUtils.isDraftRunType(event.getExperiment())) {
                getUiSupplier().get().ifPresent(ui -> ui.navigate(ExperimentView.class, event.getExperiment().getId()));
                return;
            }

            getUiSupplier().get().ifPresent(ui -> ui.getPage().getHistory().pushState(null, Routes.NEW_EXPERIMENT + "/" + event.getExperiment().getId()));
            newExperimentView.setExperiment(event.getExperiment());
        });

    }
}
