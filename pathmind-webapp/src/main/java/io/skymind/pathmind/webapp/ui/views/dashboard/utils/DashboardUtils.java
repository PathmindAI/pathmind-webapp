package io.skymind.pathmind.webapp.ui.views.dashboard.utils;

import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.DashboardItem;
import io.skymind.pathmind.shared.data.Run;

public class DashboardUtils {

	public static Stage calculateStage(DashboardItem item) {
		if (item.getModel() == null || item.getModel().isDraft()) {
			return Stage.SetUpSimulation;
		} else if (item.getLatestRun() == null) {
			return Stage.WriteRewardFunction;
		} else if (isInTrainingPhase(item.getLatestRun())) {
			return Stage.TrainPolicy;
		} else if (!item.isPolicyExported()){
			return Stage.Export;
		} else {
			return Stage.Completed;
		}
	}
	
	private static boolean isInTrainingPhase(Run run) {
		return run.getStatusEnum() != RunStatus.Completed;
	}
	
	public static boolean isTrainingStage(Stage stage) {
		return stage == Stage.TrainPolicy;
	}
	
	public static boolean isTrainingInProgress(Stage stage, Run run) {
		return isTrainingStage(stage) && RunStatus.isRunning(run.getStatusEnum());
	}
	
	public static boolean isTrainingInFailed(Stage stage, Run run) {
		return isTrainingStage(stage) && run.getStatusEnum() == RunStatus.Error;
	}
}
