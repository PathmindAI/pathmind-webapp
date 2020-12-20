package io.skymind.pathmind.webapp.ui.views.experiment.components.chart.action;

import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.CompareMetricsChartPanel;

public class RewardVariableSelectionAction {

    public static void showRewardVariable(CompareMetricsChartPanel compareMetricsChartPanel, RewardVariable rewardVariable, boolean isShow) {
        if(isShow) {
            compareMetricsChartPanel.getRewardVariableFilters().putIfAbsent(rewardVariable.getId(), rewardVariable);
        } else {
            compareMetricsChartPanel.getRewardVariableFilters().remove(rewardVariable.getId());
        }
    }
}
