package io.skymind.pathmind.webapp.ui.views.experiment.components.simulationMetrics.action;

import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;

public class SimulationRewardVariableSelectedAction {

    public static void selectRewardVariable(RewardVariable rewardVariable, ExperimentView experimentView) {

        // TODO -> STEPH -> Prevent the selection of the reward variable if it's the last one selected.
        // TODO -> FIONNA -> STEPH -> Prevent the selection of more than 2 variables. Should we really have this? I understand it can get
        // busy if we have more than two but if someone wants more why not allow. They would have to select it after the defaults.
        // TODO -> STEPH -> When selected the reward varible is not higlighted.

        // TODO -> FIONNA -> STEPH -> I left it open so that we toggle each experiment separately. It makes more sense for me to have just
        // one that is toggled for both but just in case we switch it should be possible relatively quickly'ish.
        synchronized (experimentView.getExperimentLock()) {
            experimentView.getExperiment().toggleSelectedVariable(rewardVariable);
            experimentView.updateComponents();
        }
        // Only toggle the comparison experiment is showing, meaning it's not null.
        if(experimentView.getComparisonExperiment() != null) {
            synchronized (experimentView.getComparisonExperimentLock()) {
                experimentView.getComparisonExperiment().toggleSelectedVariable(rewardVariable);
                experimentView.updateComparisonComponents();
            }
        }
    }
}
