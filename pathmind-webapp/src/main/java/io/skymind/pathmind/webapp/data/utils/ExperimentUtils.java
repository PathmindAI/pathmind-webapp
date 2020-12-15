package io.skymind.pathmind.webapp.data.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.vaadin.flow.component.UI;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.TrainingErrorDAO;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.services.training.constant.RunConstants;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentArchivedBusEvent;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentCreatedBusEvent;
import io.skymind.pathmind.webapp.bus.events.main.ExperimentFavoriteBusEvent;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;
import io.skymind.pathmind.webapp.utils.PathmindUtils;
import org.apache.commons.lang3.StringUtils;

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

    public static boolean isDraftRunType(Experiment experiment) {
        return experiment.isDraft();
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

    public static void createAndNavigateToNewExperiment(UI ui, ExperimentDAO experimentDAO, long modelId) {
        Experiment experiment = experimentDAO.createNewExperiment(modelId);
        EventBus.post(new ExperimentCreatedBusEvent(experiment));
        ui.navigate(NewExperimentView.class, experiment.getId());
    }

    public static void archiveExperiment(ExperimentDAO experimentDAO, Experiment experiment, boolean isArchive) {
        experimentDAO.archive(experiment.getId(), isArchive);
        experiment.setArchived(isArchive);
        EventBus.post(new ExperimentArchivedBusEvent(experiment));
    }

    public static void favoriteExperiment(ExperimentDAO experimentDAO, Experiment experiment, boolean newIsFavorite) {
        experimentDAO.markAsFavorite(experiment.getId(), newIsFavorite);
        experiment.setFavorite(newIsFavorite);
        EventBus.post(new ExperimentFavoriteBusEvent(experiment));
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

    public static String getIconStatus(Experiment experiment, RunStatus status) {
        if (ExperimentUtils.isDraftRunType(experiment)) {
            return "pencil";
        }
        if (RunStatus.isRunning(status)) {
            return "loading";
        } else if (status == RunStatus.Completed) {
            return "check";
        } else if (status == RunStatus.Killed || status == RunStatus.Stopping) {
            return "stopped";
        }
        return "exclamation";
    }

    public static boolean trainingEnded(Experiment experiment) {
        return experiment.getTrainingStatusEnum().getValue() >= RunStatus.Completed.getValue();
    }

    // REFACTOR -> These two methods should not be in ExperimentalUtils since it has no GUI/UI code at all but I've just temporarily put them for now and will refactor
    // them as part of my bigger refactoring.
    public static void navigateToExperiment(Optional<UI> optionalUI, Experiment experiment) {
        optionalUI.ifPresent(ui -> navigateToExperiment(ui, experiment));
    }

    public static void navigateToExperiment(UI ui, Experiment experiment) {
        ui.navigate(ExperimentUtils.isDraftRunType(experiment) ? NewExperimentView.class : ExperimentView.class, experiment.getId());
    }

    public static Optional<Experiment> getFirstUnarchivedExperiment(List<Experiment> experiments) {
        return experiments.stream()
                .filter(experiment -> !experiment.isArchived())
                .findFirst();
    }

    public static void navigateToFirstUnarchivedOrModel(Supplier<Optional<UI>> getUISupplier, List<Experiment> experiments) {
        Optional<Experiment> firstUnarchivedExperiment = ExperimentUtils.getFirstUnarchivedExperiment(experiments);
        if (firstUnarchivedExperiment.isEmpty()) {
            getUISupplier.get().ifPresent(ui -> ui.navigate(ProjectView.class, PathmindUtils.getProjectModelParameter(experiments.get(0).getProject().getId(), experiments.get(0).getModelId())));
        } else {
            getUISupplier.get().ifPresent(ui -> ExperimentUtils.navigateToExperiment(ui, firstUnarchivedExperiment.get()));
        }
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

    // TODO -> STEPH -> Temporary location to be consistent with the other methods to update the experiments internal values.
    public static void updateBestPolicy(Experiment experiment) {
        experiment.setBestPolicy(PolicyUtils.selectBestPolicy(experiment.getPolicies()).orElse(null));
    }

    public static void updateTrainingErrorAndMessage(TrainingErrorDAO trainingErrorDAO, Experiment experiment) {
        experiment.getRuns().stream()
                .filter(r -> RunStatus.isError(r.getStatusEnum()))
                .findAny()
                .ifPresent(run ->
                        trainingErrorDAO.getErrorById(run.getTrainingErrorId()).ifPresent(trainingError -> {
                            experiment.setAllowRestartTraining(trainingError.isRestartable());
                            experiment.setTrainingError(run.getRllibError() != null ? run.getRllibError() : trainingError.getDescription());
                        }));
    }

    public static void updateEarlyStopReason(Experiment experiment) {
        experiment.getRuns().stream()
                .filter(run -> StringUtils.isNotBlank(run.getSuccessMessage()) || StringUtils.isNotBlank(run.getWarningMessage()))
                .findAny()
                .ifPresent(run -> {
                    if(StringUtils.isNotBlank(run.getSuccessMessage())) {
                        experiment.setTrainingStoppedEarly(true);
                        experiment.setTrainingStoppedEarlyMessage(StringUtils.isNotBlank(run.getSuccessMessage()) ? firstLine(run.getSuccessMessage()) : firstLine(run.getWarningMessage()));
                    }
                });
    }

    private static String firstLine(String message) {
        return message.split("\\n", 2)[0];
    }

    // TODO -> STEPH -> This should really at the database DAO layer. That is when we load a full experiment we should just have everything and not have to remember to load extra data...
    public static void updateExperimentInternalValues(Experiment experiment, TrainingErrorDAO trainingErrorDAO) {
        // TODO -> STEPH -> Not sure if updateTrainingStatus() should be in the experiment class since as it needs to be done all over the code after loading the Experiment data. So many references
        // to updateTrainingStatus() in the code.
        experiment.updateTrainingStatus();
        updateBestPolicy(experiment);
        // TODO -> STEPH -> This one just tricked me up a lot tonight and so needs to be a but more obvious or setup somewhere else. Not sure if switching experiment, update, etc. will work without it.
        PolicyUtils.updateSimulationMetricsData(experiment.getBestPolicy());
        PolicyUtils.updateCompareMetricsChartData(experiment.getBestPolicy());
        // There are no extra costs if the experiment is in draft because all the values will be empty.
        updateTrainingErrorAndMessage(trainingErrorDAO, experiment);
        updateEarlyStopReason(experiment);
    }
}
