package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.action;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.views.experiment.DefaultExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

public class NavBarItemSelectExperimentAction {
    public static void selectExperiment(Experiment experiment, DefaultExperimentView defaultExperimentView) {
        // IMPORTANT -> Here we have to be a lot smarter because the action has to changed based on the view.
        if(defaultExperimentView instanceof ExperimentView) {
            selectExperimentFromExperimentView(experiment, defaultExperimentView);
        } else { // Must be NewExperimentView
            selectExperimentFromNewExperimentView(experiment, defaultExperimentView);
        }
    }

    private static void selectExperimentFromNewExperimentView(Experiment experiment, DefaultExperimentView defaultExperimentView) {
        if(experiment.isDraft()) {
            defaultExperimentView.getUI().ifPresent(ui -> ui.getPage().getHistory().pushState(null, Routes.NEW_EXPERIMENT + "/" + experiment.getId()));
            synchronized (defaultExperimentView.getExperimentLock()) {
                defaultExperimentView.setExperiment(experiment);
            }
        } else {
            defaultExperimentView.getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, experiment.getId()));
        }
    }

    private static void selectExperimentFromExperimentView(Experiment experiment, DefaultExperimentView defaultExperimentView) {
        if (experiment.isDraft()) {
            defaultExperimentView.getUI().ifPresent(ui -> ui.navigate(NewExperimentView.class, experiment.getId()));
        } else {
            defaultExperimentView.getUI().ifPresent(ui -> ui.getPage().getHistory().pushState(null, Routes.EXPERIMENT_URL + "/" + experiment.getId()));
            synchronized (defaultExperimentView.getExperimentLock()) {
                defaultExperimentView.setExperiment(experiment);
            }
        }
    }
}
