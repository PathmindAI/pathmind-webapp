package io.skymind.pathmind.ui.views.dashboard.utils;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.constants.Stage;
import io.skymind.pathmind.data.DashboardItem;
import io.skymind.pathmind.data.Run;

public class DashboardUtils {

	public static Stage calculateStage(DashboardItem item) {
		if (item.getModel() == null) {
			return Stage.SetUpSimulation;
		} else if (item.getLatestRun() == null) {
			return Stage.WriteRewardFunction;
		} else if (isInDiscoveryPhase(item.getLatestRun())) {
			return Stage.DiscoveryRunTraining;
		} else if (isInFullRunPhase(item.getLatestRun())) {
			return Stage.FullRunTraining;
		} else if (!item.isPolicyExported()){
			return Stage.Export;
		} else {
			return Stage.Completed;
		}
	}
	
	/**
	 * An item is in discovery phase, if the latest run is discovery run, and it's not completed yet
	 */
	private static boolean isInDiscoveryPhase(Run run) {
		return run.getRunTypeEnum() == RunType.DiscoveryRun && run.getStatusEnum() != RunStatus.Completed;
	}
	
	/**
	 * An item is in full run phase, if the latest run is full run, and it's not completed yet
	 * or the latest run is discovery run, and it's completed
	 */
	private static boolean isInFullRunPhase(Run run) {
		return (run.getRunTypeEnum() == RunType.FullRun && run.getStatusEnum() != RunStatus.Completed)
				|| (run.getRunTypeEnum() == RunType.DiscoveryRun && run.getStatusEnum() == RunStatus.Completed);
				
	}
}
