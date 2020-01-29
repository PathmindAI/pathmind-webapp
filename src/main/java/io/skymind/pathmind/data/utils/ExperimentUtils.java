package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.skymind.pathmind.constants.RunStatus.NotStarted;
import static io.skymind.pathmind.constants.RunType.FullRun;

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
		return experiment.getPolicies().stream()
				.map(PolicyUtils::getRunStatus)
				.min(Comparator.comparingInt(RunStatus::getValue))
				.orElse(NotStarted);
	}

	/**
	 * Returns a most significant training type for a given experiment. <br/>
	 * E.g if experiment contains test, discovery and full runs it will return {@link RunType#FullRun}
	 */
	public static RunType getTrainingType(Experiment experiment) {
		return experiment.getPolicies().stream()
				.map(Policy::getRun)
				.map(Run::getRunTypeEnum)
				.max(Comparator.comparingInt(RunType::getValue))
				.orElse(FullRun);
	}


	public static LocalDateTime getTrainingStartedDate(Experiment experiment, RunType runType) {
		return experiment.getPolicies().stream()
				.map(Policy::getRun)
				.filter(run -> run.getRunTypeEnum() == runType)
				.map(Run::getStartedAt)
				.filter(Objects::nonNull)
				.min(LocalDateTime::compareTo)
				.orElse(LocalDateTime.now());
	}

	public static LocalDateTime getTrainingOldestPolicyStartedDate(Experiment experiment, RunType runType) {
		return experiment.getPolicies().stream()
				.filter(policy -> policy.getRun().getRunTypeEnum() == runType)
				.map(Policy::getStartedAt)
				.filter(Objects::nonNull)
				.min(LocalDateTime::compareTo)
				.orElse(LocalDateTime.now());
	}

	/**
	 * Searches the most recent stopped_at date of all policies in given experiment.
	 * Returns null if any policy has not finished yet.
	 */
	public static LocalDateTime getTrainingCompletedTime(Experiment experiment) {
		final var stoppedTimes = experiment.getPolicies().stream()
				.map(Policy::getStoppedAt)
				.collect(Collectors.toList());

		if (isAnyPolicyNotFinished(stoppedTimes)) {
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

	public static double getEstimatedTrainingTime(Experiment experiment, double progress, RunType runType){
		final var earliestPolicyStartedDate = ExperimentUtils.getTrainingOldestPolicyStartedDate(experiment, runType);
		final var difference = Duration.between(earliestPolicyStartedDate, LocalDateTime.now());
		return difference.toSeconds() * (100 - progress) / progress;
	}

	private static boolean isAnyPolicyNotFinished(List<LocalDateTime> stoppedTimes) {
		return stoppedTimes.stream().anyMatch(Objects::isNull);
	}
}
