package io.skymind.pathmind.webapp.ui.views.experiment.components.narbarItem.action;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.utils.CloneUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.AbstractExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

import java.util.List;

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
            abstractExperimentView.getUI().ifPresent(ui -> ui.getPage().getHistory().pushState(null, Routes.NEW_EXPERIMENT + "/" + experiment.getId()));
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
            abstractExperimentView.getUI().ifPresent(ui -> ui.getPage().getHistory().pushState(null, Routes.EXPERIMENT + "/" + experiment.getId()));
            synchronized (abstractExperimentView.getExperimentLock()) {
                // We have to clone the selected reward variables from the original and not the one coming from the event. We have to set the selected
                // reward variables after the experiment has been loaded from the database. We also delay the rendering until the very end to avoid
                // re-rendering multiple times.
                List<RewardVariable> selectedRewardVariables = CloneUtils.shallowCloneList(abstractExperimentView.getExperiment().getSelectedRewardVariables());
                abstractExperimentView.setExperiment(experiment, false);
                abstractExperimentView.getExperiment().setSelectedRewardVariables(selectedRewardVariables);

                // If we're in the experimentView in the experimentComparison mode then we need to select the rewardVariables from the comparison experiment
                // to keep everything synchronized. This has to be done after setExperiment(experiment) above because setExperiment reloads the experiment from
                // the database because the navbar only has a partial experiment (we don't load all the experiment data for the navbar for performance reasons.
                if(abstractExperimentView instanceof ExperimentView && ((ExperimentView)abstractExperimentView).isComparisonMode()) {
                    // we have to update the experiment from the view because the experiment instance sent in is replaced by the one loaded from the DAO layer.
                    abstractExperimentView.getExperiment().setSelectedRewardVariables(
                            CloneUtils.shallowCloneList(((ExperimentView)abstractExperimentView).getComparisonExperiment().getSelectedRewardVariables()));
                }

                // The delayed rendering. This is needed for both setting the experiment, the reward variables, and possibly the comparisonExperiment if need be.
                abstractExperimentView.updateComponents();
            }
        }
    }
}
