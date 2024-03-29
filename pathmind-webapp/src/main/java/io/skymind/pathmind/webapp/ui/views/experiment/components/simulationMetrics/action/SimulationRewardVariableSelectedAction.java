package io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.action;

import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.rewardVariables.RewardVariablesRowField;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class SimulationRewardVariableSelectedAction {

    public static void selectRewardVariable(RewardVariable rewardVariable, RewardVariablesRowField rewardVariablesRowField, ExperimentView experimentView) {
        if ((rewardVariablesRowField.isSelected() && canDeselect(experimentView)) ||
            (!rewardVariablesRowField.isSelected() && canSelect(experimentView))) {
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
        if (experimentView.getComparisonExperiment() != null) {
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
