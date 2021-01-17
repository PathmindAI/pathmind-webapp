package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.action;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.views.experiment.AbstractExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

public class NavBarItemSelectExperimentAction {
    public static void selectExperiment(Experiment experiment, AbstractExperimentView abstractExperimentView) {
        // IMPORTANT -> Here we have to be a lot smarter because the action has to changed based on the view.
        if(abstractExperimentView instanceof ExperimentView) {
            selectExperimentFromExperimentView(experiment, abstractExperimentView);
        } else if(abstractExperimentView instanceof NewExperimentView) {
            selectExperimentFromNewExperimentView(experiment, abstractExperimentView);
        } else {
            throw new RuntimeException("I can't happen.");
        }
    }

    private static void selectExperimentFromNewExperimentView(Experiment experiment, AbstractExperimentView abstractExperimentView) {
        if(experiment.isDraft()) {
            abstractExperimentView.getUI().ifPresent(ui -> ui.getPage().getHistory().pushState(null, Routes.NEW_EXPERIMENT_URL + "/" + experiment.getId()));
            synchronized (abstractExperimentView.getExperimentLock()) {
                abstractExperimentView.setExperiment(experiment);
            }
        } else {
            abstractExperimentView.getUI().ifPresent(ui -> ui.navigate(ExperimentView.class, experiment.getId()));
        }
    }

    private static void selectExperimentFromExperimentView(Experiment experiment, AbstractExperimentView abstractExperimentView) {
        if (experiment.isDraft()) {
            abstractExperimentView.getUI().ifPresent(ui -> ui.navigate(NewExperimentView.class, experiment.getId()));
        } else {
            abstractExperimentView.getUI().ifPresent(ui -> ui.getPage().getHistory().pushState(null, Routes.EXPERIMENT_URL + "/" + experiment.getId()));
            synchronized (abstractExperimentView.getExperimentLock()) {
                abstractExperimentView.setExperiment(experiment);
            }
        }
    }
}
