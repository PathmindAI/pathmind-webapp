package io.skymind.pathmind.db.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import io.skymind.pathmind.db.utils.DataUtils;
import io.skymind.pathmind.db.utils.GridSortOrder;
import io.skymind.pathmind.db.utils.ModelExperimentsQueryParams;
import io.skymind.pathmind.shared.aspects.MonitorExecutionTime;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.*;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.shared.utils.PathmindNumberUtils;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ExperimentDAO {

    private final DSLContext ctx;

    ExperimentDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public Map<Long, Long> bestPoliciesForExperimentByModelId(long modelId) {
        return ExperimentRepository.bestPoliciesForExperimentByModelId(ctx, modelId);
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

    public PathmindUser getUserOfExperiment(long experimentId) {
        return ExperimentRepository.getUserByExperimentId(ctx, experimentId);
    }

    public void markAsFavorite(long experimentId, boolean isFavorite) {
        ExperimentRepository.markAsFavorite(ctx, experimentId, isFavorite);
    }

    public Optional<Experiment> getExperimentForSupportIfAllowed(long experimentId, long userId) {
        Experiment experiment = ExperimentRepository.getSharedExperiment(ctx, experimentId, userId);
        if (experiment != null) {
            loadExperimentData(experiment);
            updateExperimentInternalValues(experiment);
        }
        return Optional.ofNullable(experiment);
    }

    public Optional<Experiment> getExperimentIfAllowed(long experimentId, long userId) {
        Experiment experiment = ExperimentRepository.getExperimentIfAllowed(ctx, experimentId, userId);
        if (experiment != null) {
            loadExperimentData(experiment);
            updateExperimentInternalValues(experiment);
            setupDefaultRewardFunction(experiment);
        }
        return Optional.ofNullable(experiment);
    }

    public int getExperimentsWithRunStatusCountForUser(long userId, Collection<Integer> runStatuses) {
        return ExperimentRepository.getExperimentsWithRunStatusCountForUser(ctx, userId, CollectionUtils.emptyIfNull(runStatuses));
    }

    private void setupDefaultRewardFunction(Experiment experiment) {
        // If there is no default reward function create one and save it so that we can avoid the popup notifications
        // of saving and so on when loading a new experiment. Ideally this should be done on experiment creation
        // however older experiments may still require this logic. There is also a ticket to add a default reward
        // function on creation at: https://github.com/SkymindIO/pathmind-webapp/issues/2754
        if (StringUtils.isEmpty(experiment.getRewardFunction())) {
            experiment.setRewardFunction(ExperimentUtils.generateRewardFunction(experiment));
            ExperimentRepository.updateRewardFunction(ctx, experiment);
        }
    }

    private void loadExperimentData(Experiment experiment) {
        experiment.setModelObservations(ObservationRepository.getObservationsForModel(ctx, experiment.getModelId()));
        experiment.setSelectedObservations(ObservationRepository.getObservationsForExperiment(ctx, experiment.getId()));
        experiment.setRuns(RunRepository.getRunsForExperiment(ctx, experiment.getId()));
        experiment.setRewardVariables(RewardVariableRepository.getRewardVariablesForModel(ctx, experiment.getModelId()));
        List<SimulationParameter> simulationParameterList = SimulationParameterRepository.getSimulationParametersForExperiment(ctx, experiment.getId());
        // if there's no sim param lists for the given exp, it will use default sim params
        if (simulationParameterList == null || simulationParameterList.size() == 0) {
            simulationParameterList = SimulationParameterRepository.getSimulationParametersForModel(ctx, experiment.getModelId());
        }
        experiment.setSimulationParameters(simulationParameterList);
        ExperimentUtils.setupDefaultSelectedRewardVariables(experiment);
    }

    /**
     * This is for Project/Model page to show experiments with metric values and observations
     *
     * @param userId
     * @param modelId
     * @param offset
     * @param limit
     * @return experiments
     */
    @MonitorExecutionTime
    public List<Experiment> getExperimentsInModelForUser(long userId, long modelId, boolean isArchived, int offset, int limit, List<GridSortOrder> sortOrders) {
        String sortBy = sortOrders.size() > 0 ? sortOrders.get(0).getPropertyName() : "";
        boolean isDesc = sortOrders.size() > 0 ? sortOrders.get(0).isDescending() : false;
        var modelExperimentsQueryParams = ModelExperimentsQueryParams.builder()
                .userId(userId)
                .modelId(modelId)
                .isArchived(isArchived)
                .limit(limit)
                .offset(offset)
                .sortBy(sortBy)
                .descending(isDesc)
                .build();
        List<Experiment> experiments = ExperimentRepository.getExperimentsInModelForUser(ctx, modelExperimentsQueryParams);
        return setSelectedObservationsAndMetricsValues(ctx, experiments, modelId, userId);
    }

    private List<Experiment> setSelectedObservationsAndMetricsValues(DSLContext ctx, List<Experiment> experiments, Long modelId, Long userId) {
        Map<Long, Long> bestPoliciesId = new ConcurrentHashMap<>(this.bestPoliciesForExperimentByModelId(modelId));
        CollectionUtils.emptyIfNull(experiments).forEach(experiment -> {
            final Long policyId = bestPoliciesId.get(experiment.getId());
            experiment.setSelectedObservations(ObservationRepository.getObservationsForExperiment(ctx, experiment.getId()));
            if (policyId != null) {
                Optional<Policy> bestPolicy = PolicyRepository.getPolicyIfAllowed(ctx, policyId, userId);
                bestPolicy.ifPresent(bp -> {
                    experiment.setBestPolicy(bp);

                    List<Pair<Double, Double>> rawMetricsAvgVar = MetricsRawRepository.getMetricsRawForPolicyOptimized(ctx, policyId);

                    if (rawMetricsAvgVar != null && !rawMetricsAvgVar.isEmpty()) {
                        bp.setMetricDisplayValues(rawMetricsAvgVar.stream()
                                .map(pair -> PathmindNumberUtils.calculateUncertainty(pair.getLeft(), pair.getRight()))
                                .collect(Collectors.toList()));
                    } else {
                        bp.getMetricDisplayValues().clear();
                        for (Double metric : MetricsRawRepository.getLastIterationMetricsMeanForPolicy(ctx, policyId)) {
                            bp.getMetricDisplayValues().add(PathmindNumberUtils.formatNumber(metric));
                        }
                    }
                });
            }
        });

        return experiments;
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

    public List<Experiment> getExperimentsForModelWithPolicies(long modelId, long userId) {
        List<Experiment> experiments = getExperimentsForModel(modelId, false);
        setSelectedObservationsAndMetricsValues(ctx, experiments, modelId, userId);
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

    public void shareExperiment(long experimentId, boolean share) {
        ExperimentRepository.shareExperiment(ctx, experimentId, share);
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

    public int countExperimentsInModel(long modelId) {
        return ExperimentRepository.getExperimentCount(ctx, modelId);
    }

    public int countFilteredExperimentsInModel(long modelId, boolean isArchived) {
        return ExperimentRepository.getFilteredExperimentCount(ctx, modelId, isArchived);
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
        // Needs to be done before selectBestPolicy() is selected
        policies.forEach(policy -> policy.setScores(rewardScores.get(policy.getId())));
        PolicyUtils.selectBestPolicy(policies).ifPresent(policy -> {
            policy.setMetrics(MetricsRepository.getMetricsForPolicy(ctx, policy.getId()));
            policy.setMetricsRaws(MetricsRawRepository.getMetricsRawForPolicy(ctx, policy.getId()));
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
                .ifPresent(run -> {
                    Optional.ofNullable(TrainingErrorRepository.getErrorById(ctx, run.getTrainingErrorId())).ifPresent(trainingError -> {
                        experiment.setTrainingError(run.getRllibError() != null ? run.getRllibError() : trainingError.getDescription());
                        experiment.setTrainingErrorId(run.getTrainingErrorId());
                        experiment.setSupportArticle(trainingError.getSupportArticle());
                    });
                });
    }

    public void saveExperiment(Experiment experiment) {
        ctx.transaction(conf -> {
            DSLContext transactionCtx = DSL.using(conf);
            ObservationRepository.deleteExperimentObservations(transactionCtx, experiment.getId());
            ObservationRepository.insertExperimentObservations(transactionCtx, experiment.getId(), experiment.getSelectedObservations());
            ExperimentRepository.updateUserNotes(ctx, experiment.getId(), experiment.getUserNotes());
            ExperimentRepository.updateRewardFunction(ctx, experiment);
        });

    }

    public void setDeployPolicyOnSuccess(long id, boolean value) {
        ExperimentRepository.setDeployPolicyOnSuccess(ctx, id, value);
    }

}
