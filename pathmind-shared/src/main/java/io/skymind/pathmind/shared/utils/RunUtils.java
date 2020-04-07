package io.skymind.pathmind.shared.utils;

import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Run;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static io.skymind.pathmind.shared.services.training.constant.RunConstants.*;

public class RunUtils
{
	private RunUtils() {
	}

	public static long getElapsedTime(Run run)
	{
		if(run == null || run.getStartedAt() == null)
			return 0;

		return run.getStoppedAt() == null ?
				Duration.between(run.getStartedAt(), LocalDateTime.now()).toSeconds() :
				Duration.between(run.getStartedAt(), run.getStoppedAt()).toSeconds();
	}

	private static int getNumberOfDiscoveryRuns() {
		return TRAINING_HYPERPARAMETERS.values().stream()
				.mapToInt(List::size)
				.reduce(Math::multiplyExact)
				.orElse(0);
	}
	
	/**
	 * When a run is stopped by user, it still gets to killed status, but trainingErrorId is not assigned in this case
	 */
	public static boolean isStoppedByUser(Run run) {
		return run.getStatusEnum() == RunStatus.Killed && run.getTrainingErrorId() == 0;
	}
}
