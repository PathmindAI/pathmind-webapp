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

	public static long getTrainingElapsedTime(Experiment experiment, RunType runType) {
		return experiment.getPolicies().stream()
				.map(Policy::getRun)
				.filter(run -> run.getRunTypeEnum() == runType)
				.map(RunUtils::getElapsedTime)
				.min(Long::compareTo)
				.orElse(0L);
	}

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

	public static Integer getNumberOfProcessedIterations(Experiment experiment){
		return experiment.getPolicies().stream()
				.map(Policy::getScores)
				.map(List::size)
				.reduce(0, Integer::sum);
	}

	public static double getEstimatedTrainingTime(Experiment experiment, double progress, RunType runType){
		final var earliestPolicyStartedDate = ExperimentUtils.getTrainingStartedDate(experiment, runType);
		final var difference = Duration.between(earliestPolicyStartedDate, LocalDateTime.now());
		return difference.toSeconds() * (100 - progress) / progress;
	}

	private static boolean isAnyPolicyNotFinished(List<LocalDateTime> stoppedTimes) {
		return stoppedTimes.stream().anyMatch(Objects::isNull);
	}

	public static LocalDateTime getTrainingStoppedDate(Experiment experiment) {
		return experiment.getPolicies().stream()
				.map(Policy::getStoppedAt)
				.filter(Objects::nonNull)
				.max(LocalDateTime::compareTo)
				.orElse(LocalDateTime.now());
	}
}
