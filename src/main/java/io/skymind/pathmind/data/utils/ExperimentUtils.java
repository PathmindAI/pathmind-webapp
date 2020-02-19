package io.skymind.pathmind.data.utils;

import static io.skymind.pathmind.constants.RunStatus.NotStarted;
import static io.skymind.pathmind.constants.RunStatus.Running;
import static io.skymind.pathmind.constants.RunStatus.Starting;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;

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
		return experiment.getRuns().isEmpty();
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
			if (experiment.getPolicies().stream()
					.map(PolicyUtils::getRunStatus)
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
				.min(LocalDateTime::compareTo)
				.orElse(LocalDateTime.now());
	}

	public static LocalDateTime getTrainingEarliestRunStartedDate(Experiment experiment) {
		return experiment.getRuns().stream()
				.map(Run::getStartedAt)
				.filter(Objects::nonNull)
				.min(LocalDateTime::compareTo)
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
	 * Calculates a total number of processed iterations for policies that were initialized with given type.
	 * It sums up a size of reward list for each policy.
	 */
	public static Integer getNumberOfProcessedIterations(Experiment experiment, RunType runType){
		return experiment.getPolicies().stream()
				.filter(policy -> policy.getRun().getRunTypeEnum() == runType)
				.map(Policy::getScores)
				.map(List::size)
				.reduce(0, Integer::sum);
	}

	public static double getEstimatedTrainingTime(Experiment experiment, double progress){
		final var earliestRunStartedDate = ExperimentUtils.getTrainingEarliestRunStartedDate(experiment);
		final var difference = Duration.between(earliestRunStartedDate, LocalDateTime.now());
		return difference.toSeconds() * (100 - progress) / progress;
	}

	private static boolean isAnyNotFinished(List<LocalDateTime> stoppedTimes) {
		return stoppedTimes.stream().anyMatch(Objects::isNull);
	}
}
