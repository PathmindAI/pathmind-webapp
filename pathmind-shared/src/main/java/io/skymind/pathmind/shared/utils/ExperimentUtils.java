package io.skymind.pathmind.shared.utils;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.constants.RewardFunctionComponent;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.services.training.constant.RunConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import static io.skymind.pathmind.shared.constants.RunStatus.Error;
import static io.skymind.pathmind.shared.constants.RunStatus.NotStarted;
import static io.skymind.pathmind.shared.constants.RunStatus.Running;
import static io.skymind.pathmind.shared.constants.RunStatus.Starting;

@Slf4j
public class ExperimentUtils {

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

    public static String getProjectName(Experiment experiment) {
        return experiment.getProject().getName();
    }

    public static String getModelNumber(Experiment experiment) {
        return experiment.getModel().getName();
    }

    public static String getExperimentNumber(Experiment experiment) {
        return experiment.getName();
    }

    private static final String AL_ENGINE_ERROR_PREFIX = "RuntimeError: java.lang.RuntimeException: Engine error";

    public static boolean isAnyLogicEngineError(String rlErrorText) {
        return StringUtils.trimToEmpty(rlErrorText).startsWith(AL_ENGINE_ERROR_PREFIX);
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
    public static Integer getNumberOfProcessedIterations(Experiment experiment) {
        return experiment.getPolicies().stream()
                .map(Policy::getScores)
                .map(List::size)
                .reduce(0, Integer::sum);
    }

    public static double getEstimatedTrainingTime(Experiment experiment, double progress) {
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

    public static boolean isNewExperimentForModel(Experiment experiment, List<Experiment> experiments, long modelId) {
        return isSameModel(experiment, modelId) && !experiments.contains(experiment);
    }

    public static boolean isSameModel(Experiment experiment, long modelId) {
        return experiment != null && experiment.getModelId() == modelId;
    }

    public static boolean isSameExperiment(Experiment experiment, Experiment secondExperiment) {
        return experiment != null && experiment.getId() == secondExperiment.getId();
    }

    public static boolean isSameExperiment(Experiment experiment, long secondExperimentId) {
        return experiment != null && experiment.getId() == secondExperimentId;
    }

    public static void addOrUpdatePolicies(Experiment experiment, List<Policy> updatedPolicies) {
        updatedPolicies.forEach(updatedPolicy -> {
            if (experiment.getPolicies() == null) {
                experiment.setPolicies(new ArrayList<>());
            }
            int index = experiment.getPolicies().indexOf(updatedPolicy);
            if (index != -1) {
                experiment.getPolicies().set(index, updatedPolicy);
            } else {
                experiment.getPolicies().add(updatedPolicy);
            }
        });
    }

    public static void addOrUpdateRun(Experiment experiment, Run updatedRun) {
        if (experiment.getId() != updatedRun.getExperimentId()) {
            log.debug("Experiment ID mismatch!");
            return;
        }
        if (experiment.getRuns() == null) {
            experiment.setRuns(new ArrayList<>());
        }
        experiment.getRuns().stream()
                .filter(run -> run.getId() == updatedRun.getId())
                .findAny()
                .ifPresentOrElse(
                        run -> experiment.getRuns().set(experiment.getRuns().indexOf(run), updatedRun),
                        () -> experiment.getRuns().add(updatedRun));
    }

    public static void addOrUpdateRuns(Experiment experiment, List<Run> updatedRuns) {
        updatedRuns.forEach(updatedRun ->
                addOrUpdateRun(experiment, updatedRun));
    }

    public static void updatedRunsForPolicies(Experiment experiment, List<Run> runs) {
        if (experiment.getPolicies() == null) {
            return;
        }
        runs.forEach(run ->
                updatedRunForPolicies(experiment, run));
    }

    public static void updatedRunForPolicies(Experiment experiment, Run run) {
        experiment.getPolicies().stream()
                .filter(policy -> policy.getRunId() == run.getId())
                .forEach(policy -> policy.setRun(run));
    }

    public static boolean isRunning(Experiment experiment) {
        if (experiment.getRuns() == null || experiment.getRuns().isEmpty()) {
            return false;
        }
        return experiment.getRuns().stream()
                .anyMatch(run -> RunStatus.isRunning(run.getStatusEnum()));
    }

    public static boolean isNotStarted(Experiment experiment) {
        if (experiment.getRuns() == null || experiment.getRuns().isEmpty()) {
            return false;
        }
        return experiment.getRuns().stream()
                .anyMatch(run -> RunStatus.isRunning(run.getStatusEnum()));
    }

    public static boolean trainingEnded(Experiment experiment) {
        return experiment.getTrainingStatusEnum().getValue() >= RunStatus.Completed.getValue();
    }

    public static Optional<Experiment> getFirstUnarchivedExperiment(List<Experiment> experiments) {
        return experiments.stream()
                .filter(experiment -> !experiment.isArchived())
                .findFirst();
    }

    /**
     * Replace the existing experiment in the experiments list without replicating the list (for example using map and then collecting)
     * as well as keeping the exact same order in case the list is already sorted.
     */
    public static void updateExperimentInExperimentsList(List<Experiment> experiments, Experiment experiment) {
        int index = IntStream.range(0, experiments.size())
                .filter(x -> experiment.getId() == experiments.get(x).getId())
                .findFirst().orElse(-1);
        if (index > -1) {
            experiments.set(index, experiment);
        }
    }

    public static void updateBestPolicy(Experiment experiment) {
        experiment.setBestPolicy(PolicyUtils.selectBestPolicy(experiment.getPolicies()).orElse(null));
    }

    public static void updateEarlyStopReason(Experiment experiment) {
        experiment.getRuns().stream()
                .filter(run -> StringUtils.isNotBlank(run.getSuccessMessage()) || StringUtils.isNotBlank(run.getWarningMessage()))
                .findAny()
                .ifPresent(run -> {
                    if (StringUtils.isNotBlank(run.getSuccessMessage())) {
                        experiment.setTrainingStoppedEarly(true);
                        experiment.setTrainingStoppedEarlyMessage(StringUtils.isNotBlank(run.getSuccessMessage()) ? firstLine(run.getSuccessMessage()) : firstLine(run.getWarningMessage()));
                    }
                });
    }

    private static String firstLine(String message) {
        return message.split("\\n", 2)[0];
    }

    public static void updateTrainingStatus(Experiment experiment) {
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

        experiment.setTrainingStatusEnum(status);
    }

    public static void updateExperimentInternals(Experiment experiment) {
        updateBestPolicy(experiment);
        if (experiment.getBestPolicy() != null) {
            PolicyUtils.updateSimulationMetricsData(experiment.getBestPolicy());
            PolicyUtils.updateCompareMetricsChartData(experiment.getBestPolicy());
        }
        updateTrainingStatus(experiment);
    }

    public static void setupDefaultSelectedRewardVariables(Experiment experiment) {
        experiment.getRewardVariables().stream()
                .filter(rewardVariable -> rewardVariable != null)
                .filter(rewardVariable -> rewardVariable.getArrayIndex() < RewardVariable.DEFAULT_SELECTED_REWARD_VARIABLES)
                .forEach(rewardVariable -> experiment.addSelectedRewardVariable(rewardVariable));
        Collections.sort(experiment.getSelectedRewardVariables(), Comparator.comparing(RewardVariable::getArrayIndex));
    }

    public static String generateRewardFunction(Experiment experiment) {
        if (!experiment.isHasGoals())
            return "";

        StringBuilder sb = new StringBuilder("// Here's a suggested reward function to get started\n");

        for (RewardVariable rv : experiment.getRewardVariables()) {
            GoalConditionType goal = rv.getGoalConditionTypeEnum();
            if (goal != null) {
                RewardFunctionComponent functionComponent = goal.getRewardFunctionComponent();
                switch (rv.getDataType()) {
                    case "boolean": {
                        sb.append(
                                MessageFormat.format(
                                        "reward {1}= after.{0} ? 1 : 0; // {2} {0}",
                                        rv.getName(), // 0
                                        functionComponent.getMathOperation(), // 1
                                        functionComponent.getComment() // 2
                                )
                        );
                        break;
                    }
                    default: {
                        sb.append(
                                MessageFormat.format(
                                        "reward {1}= after.{0} - before.{0}; // {2} {0}",
                                        rv.getName(), // 0
                                        functionComponent.getMathOperation(), // 1
                                        functionComponent.getComment() // 2
                                )
                        );
                    }
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
