package io.skymind.pathmind.db.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.skymind.pathmind.db.utils.DashboardQueryParams;
import io.skymind.pathmind.db.utils.DataUtils;
import io.skymind.pathmind.shared.aspects.MonitorExecutionTime;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.DashboardItem;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Metrics;
import io.skymind.pathmind.shared.data.MetricsRaw;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardScore;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import static io.skymind.pathmind.db.utils.DashboardQueryParams.QUERY_TYPE.FETCH_MULTIPLE_BY_USER;
import static io.skymind.pathmind.db.utils.DashboardQueryParams.QUERY_TYPE.FETCH_SINGLE_BY_EXPERIMENT;

@Repository
public class ExperimentDAO {
    private final DSLContext ctx;

    ExperimentDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public Optional<Experiment> getExperiment(long experimentId) {
        Experiment experiment = ExperimentRepository.getExperiment(ctx, experimentId);
        return Optional.ofNullable(experiment);
    }

    public Optional<Experiment> getExperimentWithRuns(long experimentId) {
        Experiment experiment = ExperimentRepository.getExperiment(ctx, experimentId);
        Optional<Experiment> result = Optional.ofNullable(experiment);
        result.ifPresent(e -> {
            List<Run> runsForExperiment = RunRepository.getRunsForExperiment(ctx, experiment.getId());
            e.setRuns(runsForExperiment);
        });
        return result;
    }

    public void markAsFavorite(long experimentId, boolean isFavorite) {
        ExperimentRepository.markAsFavorite(ctx, experimentId, isFavorite);
    }

    public Optional<Experiment> getExperimentForSupportIfAllowed(long experimentId, long userId) {
        Experiment experiment = ExperimentRepository.getSharedExperiment(ctx, experimentId, userId);
        if(experiment != null) {
            loadExperimentData(experiment);
        }
        return Optional.ofNullable(experiment);
    }

    public Optional<Experiment> getExperimentIfAllowed(long experimentId, long userId) {
        Experiment experiment = ExperimentRepository.getExperimentIfAllowed(ctx, experimentId, userId);
        if(experiment != null) {
            loadExperimentData(experiment);
            updateExperimentInternalValues(experiment);
        }
        return Optional.ofNullable(experiment);
    }

    private void loadExperimentData(Experiment experiment) {
        experiment.setModelObservations(ObservationRepository.getObservationsForModel(ctx, experiment.getModelId()));
        experiment.setSelectedObservations(ObservationRepository.getObservationsForExperiment(ctx, experiment.getId()));
        experiment.setRuns(RunRepository.getRunsForExperiment(ctx, experiment.getId()));
        experiment.setRewardVariables(RewardVariableRepository.getRewardVariablesForModel(ctx, experiment.getModelId()));
        ExperimentUtils.setupDefaultSelectedRewardVariables(experiment);
    }

    public List<Experiment> getExperimentsForModel(long modelId) {
        return getExperimentsForModel(modelId, true);
    }

    /**
     * Used as a quick check before a full experiment load to see if we're on the right view between ExperimentView and NewExperimentView.
     */
    public boolean isDraftExperiment(long experimentId) {
        return RunRepository.getNumberOfRunsForExperiment(ctx, experimentId) == 0;
    }

    public List<Experiment> getExperimentsForModel(long modelId, boolean isIncludeArchived) {
        List<Experiment> experiments = ExperimentRepository.getExperimentsForModel(ctx, modelId, isIncludeArchived);
        addRunsToExperiments(experiments);
        return experiments;
    }

    private void addRunsToExperiments(List<Experiment> experiments) {
        Map<Long, List<Run>> runsGroupedByExperiment = RunRepository.getRunsForExperiments(ctx, DataUtils.convertToIds(experiments));
        experiments.stream().forEach(experiment ->
                experiment.setRuns(runsGroupedByExperiment.get(experiment.getId())));
        // A quick solution to fix a bug due to null checks not being implemented throughout the app.
        experiments.stream()
                .filter(experiment -> experiment.getRuns() == null)
                .forEach(experiment -> experiment.setRuns(new ArrayList<>()));
    }

    public void shareExperimentWithSupport(long experimentId) {
        ExperimentRepository.shareExperimentWithSupport(ctx, experimentId);
    }

    public void updateRewardFunction(Experiment experiment) {
        ExperimentRepository.updateRewardFunction(ctx, experiment);
    }

    public void updateTrainingStatus(DSLContext transactionCtx, Experiment experiment) {
        ExperimentRepository.updateTrainingStatus(transactionCtx, experiment);
    }

    public void archive(long experimentId, boolean isArchive) {
        ExperimentRepository.archive(ctx, experimentId, isArchive);
    }

    @MonitorExecutionTime
    public List<DashboardItem> getDashboardItemsForUser(long userId, int offset, int limit) {
        var dashboardQueryParams = DashboardQueryParams.builder()
                .userId(userId)
                .limit(limit)
                .offset(offset)
                .queryType(FETCH_MULTIPLE_BY_USER)
                .build();
        return ExperimentRepository.getDashboardItems(ctx, dashboardQueryParams);
    }

    @MonitorExecutionTime
    public List<DashboardItem> getSingleDashboardItem(long experimentId) {
        var dashboardQueryParams = DashboardQueryParams.builder()
                .experimentId(experimentId)
                .limit(1)
                .offset(0)
                .queryType(FETCH_SINGLE_BY_EXPERIMENT)
                .build();
        return ExperimentRepository.getDashboardItems(ctx, dashboardQueryParams);
    }

    @MonitorExecutionTime
    public int countDashboardItemsForUser(long userId) {
        return ExperimentRepository.countDashboardItemsForUser(ctx, userId);
    }

    public Experiment createNewExperiment(long modelId) {
        return ctx.transactionResult(conf -> {
            DSLContext transactionCtx = DSL.using(conf);
            String experimentName = Integer.toString(ExperimentRepository.getExperimentCount(transactionCtx, modelId) + 1);
            Experiment lastExperiment = ExperimentRepository.getLastExperimentForModel(transactionCtx, modelId);
            String rewardFunction = lastExperiment != null ? lastExperiment.getRewardFunction() : "";
            boolean hasGoals = lastExperiment != null ? lastExperiment.isHasGoals() : false;
            List<Observation> observations = lastExperiment != null ? ObservationRepository.getObservationsForExperiment(transactionCtx, lastExperiment.getId()) : Collections.emptyList();
            Experiment exp = ExperimentRepository.createNewExperiment(transactionCtx, modelId, experimentName, rewardFunction, hasGoals);
            ObservationRepository.insertExperimentObservations(transactionCtx, exp.getId(), observations);
            exp.setSelectedObservations(observations);
            return exp;
        });
    }

    public void updateUserNotes(long experimentId, String userNotes) {
        ExperimentRepository.updateUserNotes(ctx, experimentId, userNotes);
    }

    public Optional<Experiment> getFullExperiment(long experimentId) {
        Optional<Experiment> optionalExperiment = getExperiment(experimentId);
        optionalExperiment.ifPresent(experiment -> {
            updateExperimentInternalValues(experiment);
        });
        return optionalExperiment;
    }

    private void updateExperimentInternalValues(Experiment experiment) {
        experiment.setRuns(RunRepository.getRunsForExperiment(ctx, experiment.getId()));
        experiment.setPolicies(loadPoliciesForExperiment(ctx, experiment.getId()));

        ExperimentUtils.updateExperimentInternals(experiment);

        // There are no extra costs if the experiment is in draft because all the values will be empty.
        updateTrainingErrorAndMessage(ctx, experiment);
        ExperimentUtils.updateEarlyStopReason(experiment);
    }

    private List<Policy> loadPoliciesForExperiment(DSLContext ctx, long experimentId) {
        List<Policy> policies = PolicyRepository.getPoliciesForExperiment(ctx, experimentId);
        Map<Long, List<RewardScore>> rewardScores = RewardScoreRepository.getRewardScoresForPolicies(ctx, DataUtils.convertToIds(policies));
        Map<Long, List<Metrics>> metricsMap = MetricsRepository.getMetricsForPolicies(ctx, DataUtils.convertToIds(policies));
        Map<Long, List<MetricsRaw>> metricsRawMap = MetricsRawRepository.getMetricsRawForPolicies(ctx, DataUtils.convertToIds(policies));
        policies.forEach(policy -> {
            long id = policy.getId();
            policy.setScores(rewardScores.get(id));
            policy.setMetrics(metricsMap.get(id));
            policy.setMetricsRaws(metricsRawMap.get(id));
        });
        return policies;
    }

    public void updateTrainingErrorAndMessage(Experiment experiment) {
        updateTrainingErrorAndMessage(ctx, experiment);
    }

    // REFACTOR -> These should really be cached rather than requiring the same database calls over and over and over. At the very least we can just see if they
    // are in memory and if so use that instead of search. See: https://github.com/SkymindIO/pathmind-webapp/issues/2599
    private void updateTrainingErrorAndMessage(DSLContext ctx, Experiment experiment) {
        experiment.getRuns().stream()
                .filter(r -> RunStatus.isError(r.getStatusEnum()))
                .findAny()
                .ifPresent(run ->
                        Optional.ofNullable(TrainingErrorRepository.getErrorById(ctx, run.getTrainingErrorId())).ifPresent(trainingError -> {
                            experiment.setAllowRestartTraining(trainingError.isRestartable());
                            experiment.setTrainingError(run.getRllibError() != null ? run.getRllibError() : trainingError.getDescription());
                        }));
    }
}
