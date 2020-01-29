package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Run;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static io.skymind.pathmind.constants.RunType.DiscoveryRun;
import static io.skymind.pathmind.services.training.constant.RunConstants.*;

public class RunUtils
{
	public final static String TEMPORARY_POSTFIX = "TEMP";

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

	public static int getNumberOfTrainingIterationsForRunType(RunType runType) {
		if(runType == DiscoveryRun) {
			return DISCOVERY_RUN_ITERATIONS * getNumberOfDiscoveryRuns();
		}
		return FULL_RUN_ITERATIONS;
	}

	private static int getNumberOfDiscoveryRuns() {
		return TRAINING_HYPERPARAMETERS.values().stream()
				.mapToInt(List::size)
				.reduce(Math::multiplyExact)
				.orElse(0);
	}
}
