package io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.action;

import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.rewardVariables.RewardVariablesRowField;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class SimulationRewardVariableSelectedAction {

    // TODO -> FIONNA -> STEPH -> (STEPH) I left it open so that we toggle each experiment separately. It makes more sense for me to have just
    // one reward variable toggle for both so I kept it simpler for now but just in case it should be possible to switch with only
    // medium effort. The hard part will be knowing which experiment is being updated whereas now we update both the main experiment
    // and the comparison experiment at the same time. Since it is very likely to be on the same reward variables I didn't go through
    // that effort yet.
    // TODO -> FIONNA -> STEPH -> (STEPH) If we want to put an popup dialog to say no you can't do this then we should
    // do it at this level, that do a stream over all the fields and filter for those that are and aren't already selected
    // and then enable a popup dialog because the decision is really made based on the conditions at this level rather
    // than on the individual components. For now though I'm ignoring it and assuming the ability to just not be able to click
    // will work as this is what is in dev.
    public static void selectRewardVariable(RewardVariable rewardVariable, RewardVariablesRowField rewardVariablesRowField, ExperimentView experimentView) {
        if (rewardVariablesRowField.isSelected() && canDeselect(experimentView)) {
            toggleRow(rewardVariable, experimentView);
        } else if (!rewardVariablesRowField.isSelected() && canSelect(experimentView)){
            toggleRow(rewardVariable, experimentView);
        }
    }

    /**
     * The components will automatically be updated with experimentView.updateComponents() method which is required because other components such as the
     * chart also need to be updated.
     */
    private static void toggleRow(RewardVariable rewardVariable, ExperimentView experimentView) {
        toggleExperiment(rewardVariable, experimentView);
        toggleComparisonExperiment(rewardVariable, experimentView);
    }

    private static void toggleComparisonExperiment(RewardVariable rewardVariable, ExperimentView experimentView) {
        // Only toggle the comparison experiment is showing, meaning it's not null.
        if(experimentView.getComparisonExperiment() != null) {
            synchronized (experimentView.getComparisonExperimentLock()) {
                experimentView.getComparisonExperiment().toggleSelectedVariable(rewardVariable);
                experimentView.updateComparisonComponents();
            }
        }
    }

    private static void toggleExperiment(RewardVariable rewardVariable, ExperimentView experimentView) {
        synchronized (experimentView.getExperimentLock()) {
            experimentView.getExperiment().toggleSelectedVariable(rewardVariable);
            experimentView.updateComponents();
        }
    }

    private static boolean canSelect(ExperimentView experimentView) {
        return experimentView.getExperiment().getSelectedRewardVariables().size() < RewardVariable.MAX_SELECTED_REWARD_VARIABLES;
    }
    private static boolean canDeselect(ExperimentView experimentView) {
        return experimentView.getExperiment().getSelectedRewardVariables().size() > RewardVariable.MIN_SELECTED_REWARD_VARIABLES;
    }
}
