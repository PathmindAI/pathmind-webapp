package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.action;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.views.experiment.DefaultExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

public class NewExperimentSelectAction {

    public static void selectExperiment(Experiment experiment, DefaultExperimentView defaultExperimentView) {
        NewExperimentView newExperimentView = (NewExperimentView)defaultExperimentView;
        // First save draft before switching as part of the auto-save functionality.
        newExperimentView.saveDraftExperiment(() -> {
            // If it's a not a new draft experiment then we just want to go to the experiment view and ignore this.
            if (!experiment.isDraft()) {
                newExperimentView.getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, experiment.getId()));
            } else {
                newExperimentView.getUI().ifPresent(ui -> ui.getPage().getHistory().pushState(null, Routes.NEW_EXPERIMENT + "/" + experiment.getId()));
                newExperimentView.setExperiment(experiment);
            }
        });
    }
}
