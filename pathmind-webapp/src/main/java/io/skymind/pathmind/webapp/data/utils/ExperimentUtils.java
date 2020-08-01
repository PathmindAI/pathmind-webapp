package io.skymind.pathmind.webapp.data.utils;

import static io.skymind.pathmind.shared.constants.RunStatus.NotStarted;
import static io.skymind.pathmind.shared.constants.RunStatus.Running;
import static io.skymind.pathmind.shared.constants.RunStatus.Starting;
import static io.skymind.pathmind.shared.constants.RunStatus.Error;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.services.training.constant.RunConstants;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.ExperimentCreatedBusEvent;
import io.skymind.pathmind.webapp.bus.events.ExperimentUpdatedBusEvent;
import io.skymind.pathmind.webapp.bus.events.RunUpdateBusEvent;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

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
		return experiment.isDraft();
	}

	public static boolean isFavorite(Experiment experiment) {
		return experiment.isFavorite();
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

        if (status == RunStatus.Killed) {
            if (experiment.getRuns().stream()
                    .map(Run::getTrainingErrorId)
                    .anyMatch(errorId -> errorId > 0)) {
                status = Error;
            }
        }
		return status;
	}

	public static LocalDateTime getTrainingStartedDate(Experiment experiment) {
		return experiment.getRuns().stream()
				.map(Run::getStartedAt)
				.filter(Objects::nonNull)
				// experiment can have multiple run, in case of a restart
				// so we take the first one, this will make sure we show
                // the correct elapsed time to the user
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
        long totalSeconds = experiment.getRuns().stream()
                .map(r -> {
                    LocalDateTime startTime = r.getEc2CreatedAt() != null ? r.getEc2CreatedAt() : r.getStartedAt();
                    LocalDateTime endTime = r.getStoppedAt() != null ? r.getStoppedAt() : LocalDateTime.now();
                    return Duration.between(startTime, endTime).toSeconds();
                })
                .reduce(Long::sum)
                .orElse(0L);
        return calculateTrainingSecondsLeft(totalSeconds, progress);
	}

	private static double calculateTrainingSecondsLeft(long totalSeconds, double progress) {
		return totalSeconds * (100 - progress) / progress;
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

    public static void createAndNavigateToNewExperiment(UI ui, ExperimentDAO experimentDAO, long modelId) {
        Experiment experiment = experimentDAO.createNewExperiment(modelId);
        EventBus.post(new ExperimentCreatedBusEvent(experiment));
        ui.navigate(NewExperimentView.class, experiment.getId());
    }

    public static void archiveExperiment(ExperimentDAO experimentDAO, Experiment experiment, boolean isArchive) {
	    experimentDAO.archive(experiment.getId(), isArchive);
	    EventBus.post(new ExperimentUpdatedBusEvent(experiment));
    }

    public static void favoriteExperiment(ExperimentDAO experimentDAO, Experiment experiment, boolean newIsFavorite) {
        experimentDAO.markAsFavorite(experiment.getId(), newIsFavorite);
        EventBus.post(new ExperimentUpdatedBusEvent(experiment));
    }

    public static boolean isNewExperimentForModel(Experiment experiment, List<Experiment> experiments, long modelId) {
        return isSameModel(experiment, modelId) && !experiments.contains(experiment);
    }

    public static boolean isSameModel(Experiment experiment, long modelId) {
        return experiment != null && experiment.getModelId() == modelId;
    }

    public static boolean isSameExperiment(Experiment experiment, Experiment secondExperiment) {
        return experiment != null && experiment.getId() == secondExperiment.getId();
    }

    public static void addOrUpdateRun(Experiment experiment, Run updatedRun) {
        experiment.getRuns().stream()
                .filter(run -> run.getId() == updatedRun.getId())
                .findAny()
                .ifPresentOrElse(
                        run -> experiment.getRuns().set(experiment.getRuns().indexOf(run), updatedRun),
                        () -> experiment.getRuns().add(updatedRun));
    }

    public static void updatedRunForPolicies(Experiment experiment, Run run) {
        experiment.getPolicies().stream()
                .filter(policy -> policy.getRunId() == run.getId())
                .forEach(policy -> policy.setRun(run));
    }
}
