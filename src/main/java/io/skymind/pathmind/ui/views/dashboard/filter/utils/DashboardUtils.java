package io.skymind.pathmind.ui.views.dashboard.filter.utils;

import java.util.List;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.constants.Stage;
import io.skymind.pathmind.data.DashboardItem;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.utils.ExperimentUtils;

public class DashboardUtils {

	public static Stage calculateStage(DashboardItem item) {
		if (item.getModel() == null) {
			return Stage.SetUpSimulation;
		} else if (ExperimentUtils.isDraftRunType(item.getExperiment())) {
			return Stage.WriteRewardFunction;
		} else if (!hasCompletedRunOfType(item.getExperiment().getRuns(), RunType.DiscoveryRun)) {
			return Stage.DiscoveryRunTraining;
		} else if (!hasCompletedRunOfType(item.getExperiment().getRuns(), RunType.FullRun)) {
			return Stage.FullRunTraining;
		} else {
			return Stage.Export;
		}
	}
	
	private static boolean hasCompletedRunOfType(List<Run> runs, RunType runType) {
		return runs.stream().anyMatch(run -> run.getRunTypeEnum() == runType && run.getStatusEnum() == RunStatus.Completed);
	}
}
