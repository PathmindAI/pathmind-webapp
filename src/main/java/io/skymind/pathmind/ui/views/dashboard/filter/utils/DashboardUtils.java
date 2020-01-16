package io.skymind.pathmind.ui.views.dashboard.filter.utils;

import java.util.List;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.constants.Stage;
import io.skymind.pathmind.data.DashboardItem;
import io.skymind.pathmind.data.Run;

public class DashboardUtils {

	public static Stage calculateStage(DashboardItem item) {
		if (item.getModel() == null) {
			return Stage.SetUpSimulation;
		} else if (!hasAnyRuns(item.getExperiment().getRuns())) {
			return Stage.WriteRewardFunction;
		} else if (!hasCompletedRunOfType(item.getExperiment().getRuns(), RunType.DiscoveryRun)) {
			return Stage.DiscoveryRunTraining;
		} else if (!hasCompletedRunOfType(item.getExperiment().getRuns(), RunType.FullRun)) {
			return Stage.FullRunTraining;
		} else {
			return Stage.Export;
		}
	}
	
	//TODO: Change me after BE fix
	public static boolean hasAnyRuns(List<Run> runs) {
		return runs.stream().anyMatch(run -> run.getId() > 0 && run.getExperimentId() > 0);
	}
	
	//TODO: Change me after BE fix
	private static boolean hasCompletedRunOfType(List<Run> runs, RunType runType) {
		return runs.stream().anyMatch(run -> run.getId() > 0 && run.getExperimentId() > 0 && run.getRunTypeEnum() == runType && run.getStatusEnum() == RunStatus.Completed);
	}
}
