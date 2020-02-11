package io.skymind.pathmind.webapp.data.utils;

import static io.skymind.pathmind.shared.constants.RunStatus.NotStarted;
import static io.skymind.pathmind.shared.constants.RunStatus.Running;
import static io.skymind.pathmind.shared.constants.RunStatus.Starting;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.db.data.Experiment;
import io.skymind.pathmind.db.data.Policy;
import io.skymind.pathmind.db.data.Run;
import io.skymind.pathmind.shared.services.training.constant.RunConstants;

public class ExperimentUtils
{
	private ExperimentUtils() {
	}

	public static Experiment generateNewDefaultExperiment(long modelId, String name, String rewardFunction) {
		Experiment newExperiment = new Experiment();
		newExperiment.setDateCreated(LocalDateTime.now());
		newExperiment.setModelId(modelId);
		newExperiment.setName(name);
		newExperiment.setRewardFunction(rewardFunction);
		return newExperiment;
	}

	public static boolean isDraftRunType(Experiment experiment) {
		return experiment.getRuns() == null || experiment.getRuns().isEmpty();
	}

	public static String getProjectName(Experiment experiment) {
		return experiment.getProject().getName();
	}

	public static String getModelNumber(Experiment experiment) {
		return experiment.getModel().getName();
	}

	public static String getExperimentNumber(Experiment experiment) {
		return experiment.getName();
	}

	public static RunStatus getTrainingStatus(Experiment experiment) {
		RunStatus status = experiment.getRuns().stream()
				.map(Run::getStatusEnum)
				.min(Comparator.comparingInt(RunStatus::getValue))
				.orElse(NotStarted);
		
		// In Running status, there can be some runs completed while others are yet to be started
		// So checking that to make sure
		if (status == NotStarted || status == Starting) {
			if (experiment.getRuns().stream()
					.map(Run::getStatusEnum)
					.map(RunStatus::getValue)
					.anyMatch(statusVal -> statusVal > Starting.getValue())) {
				status = Running;
			}
		}
		return status;
	}

	public static LocalDateTime getTrainingStartedDate(Experiment experiment) {
		return experiment.getRuns().stream()
				.map(Run::getStartedAt)
				.filter(Objects::nonNull)
				// experiment can have multiple run, in case of a restart
				// so we take the latest one
				.max(LocalDateTime::compareTo)
				.orElse(LocalDateTime.now());
	}

	/**
	 * Searches the most recent stopped_at date of all runs in given experiment.
	 * Returns null if any policy has not finished yet.
	 */
	public static LocalDateTime getTrainingCompletedTime(Experiment experiment) {
		final var stoppedTimes = experiment.getRuns().stream()
				.map(Run::getStoppedAt)
				.collect(Collectors.toList());

		if (isAnyNotFinished(stoppedTimes)) {
			return null;
		}

		return stoppedTimes.stream()
				.max(LocalDateTime::compareTo)
				.orElse(LocalDateTime.now());
	}

	/**
	 * Calculates a total number of processed iterations for policies.
	 * It sums up a size of reward list for each policy.
	 */
	public static Integer getNumberOfProcessedIterations(Experiment experiment){
		return experiment.getPolicies().stream()
				.map(Policy::getScores)
				.map(List::size)
				.reduce(0, Integer::sum);
	}

	public static double getEstimatedTrainingTime(Experiment experiment, double progress){
		final var earliestRunStartedDate = ExperimentUtils.getTrainingStartedDate(experiment);
		return calculateTrainingSecondsLeft(earliestRunStartedDate, progress);
	}

	public static double getEstimatedTrainingTime(LocalDateTime startedDate, double progress) {
		return calculateTrainingSecondsLeft(startedDate, progress);
	}

	private static double calculateTrainingSecondsLeft(LocalDateTime startedDate, double progress) {
		var difference = Duration.between(startedDate, LocalDateTime.now()).toSeconds();
		difference = subtractPretrainingTime(difference);
		return difference * (100 - progress) / progress;
	}

	/**
	 * To make ETA more realistic we subtract 15 minutes as pre-training warm-up time
	 */
	private static long subtractPretrainingTime(long difference) {
		long subtractedTime = difference - TimeUnit.MINUTES.toSeconds(15);
		return subtractedTime > 0 ? subtractedTime : difference;
	}

	public static double getEstimatedTrainingTimeForSingleRun(Run run, double progress) {
		final var difference = Duration.between(run.getStartedAt(), LocalDateTime.now());
		return difference.toSeconds() * (100 - progress) / progress;
	}

	private static boolean isAnyNotFinished(List<LocalDateTime> stoppedTimes) {
		return stoppedTimes.stream().anyMatch(Objects::isNull);
	}
	
	public static double calculateProgressByExperiment(Experiment experiment) {
		Integer iterationsProcessed = getNumberOfProcessedIterations(experiment);
		return calculateProgressByIterationsProcessed(iterationsProcessed);
	}

	public static double calculateProgressByIterationsProcessed(Integer iterationsProcessed) {
		double totalIterations = RunConstants.PBT_RUN_ITERATIONS * RunConstants.PBT_NUM_SAMPLES;
		double progress = (iterationsProcessed / totalIterations) * 100;
		return Math.max(Math.min(100d, progress), 0);
	}
}
