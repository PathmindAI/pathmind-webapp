package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.action;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.views.experiment.DefaultExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

public class ExperimentSelectAction {

    public static void selectExperiment(Experiment experiment, DefaultExperimentView defaultExperimentView) {
        ExperimentView experimentView = (ExperimentView)defaultExperimentView;
        // If it's a new draft experiment then we just want to go to the draft view and ignore this.
        if (experiment.isDraft()) {
            experimentView.getUI().ifPresent(ui -> ui.navigate(NewExperimentView.class, experiment.getId()));
        } else {
            experimentView.getUI().ifPresent(ui -> ui.getPage().getHistory().pushState(null, Routes.EXPERIMENT_URL + "/" + experiment.getId()));
            experimentView.setExperiment(experiment);
        }
    }
}
