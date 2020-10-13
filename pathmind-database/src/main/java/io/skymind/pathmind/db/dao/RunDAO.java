package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.db.utils.DBUtils;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.constants.RunType;
import io.skymind.pathmind.shared.data.*;
import io.skymind.pathmind.shared.data.user.UserMetrics;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class RunDAO {

    private final DSLContext ctx;

    public RunDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public Run getRun(long runId) {
        return RunRepository.getRun(ctx, runId);
    }

    public List<Run> getRunsForExperiment(Experiment experiment) {
        return RunRepository.getRunsForExperiment(ctx, experiment.getId());
    }

    public Run createRun(DSLContext transactionCtx, Experiment experiment, RunType runType){
    	RunRepository.clearNotificationSentInfo(transactionCtx, experiment.getId());
        return RunRepository.createRun(transactionCtx, experiment, runType);
    }

    public void markAsStarting(DSLContext transactionCtx, long runId, String jobId){
        RunRepository.markAsStarting(transactionCtx, runId, jobId);
    }

    /**
     * Returns true if
     * - there is no other run with same run type that still executing
     * - notification is not sent yet for any other run
     */
    public boolean shouldSendNotification(long experimentId, int runType) {
        return RunRepository.getAlreadyNotifiedOrStillExecutingRunsWithType(ctx, experimentId, runType).isEmpty();
    }

    public void markAsNotificationSent(long runId) {
        RunRepository.markAsNotificationSent(ctx, runId);
    }

    /**
     * This is used in case a run is restarted, so that Notification Sent value is cleared
     * and a notification can be sent again after the training is completed
     */
    public void clearNotificationSentInfo(long experimentId) {
        RunRepository.clearNotificationSentInfo(ctx, experimentId);
    }

    public List<Run> getExecutingRuns(int completingAttempts) {
        return RunRepository.getExecutingRuns(ctx, completingAttempts);
    }

    public Map<Long, List<String>> getStoppedPolicyNamesForRuns(List<Long> runIds) {
        return RunRepository.getStoppedPolicyNamesForRuns(ctx, runIds);
    }

    public void markAsStopping(DSLContext transactionCtx, Run run) {
        updateRun(run, ProviderJobStatus.STOPPING, transactionCtx);
        updateExperiment(run, transactionCtx);
    }

    private void updateRun(DSLContext transactionCtx, Run run, ProviderJobStatus status, List<Policy> policies) {
        updateRun(run, status, transactionCtx);
        updateExperiment(run, transactionCtx);
        if (!policies.isEmpty()) {
            updatePolicies(run, policies, transactionCtx);
        }
    }

    private void updateExperiment(Run run, DSLContext transactionCtx) {
        ExperimentRepository.updateLastActivityDate(transactionCtx, run.getExperimentId());
    }

    private void updateRun(Run run, ProviderJobStatus jobStatus, DSLContext transactionCtx) {
        final var newRunStatus = jobStatus.getRunStatus();

        RunStatus currentRunStatus = run.getStatusEnum();

        // IMPORTANT -> Needed for both the updateStatus and EventBus post.
        run.setStatusEnum(newRunStatus);
        if (newRunStatus == RunStatus.Running && newRunStatus != currentRunStatus) {
            run.setEc2CreatedAt(LocalDateTime.now());
        }

        // STEPH -> REFACTOR -> QUESTION -> Isn't this just a duplicate of setStoppedAtForFinishedPolicies()
        run.setStoppedAt(RunStatus.isRunning(newRunStatus) ? null : LocalDateTime.now());
        RunRepository.updateStatus(transactionCtx, run);
    }

    private void updatePolicies(Run run, List<Policy> policies, DSLContext transactionCtx) {
    	// We need this line because the policies are generated from the progress string from the backend and are NOT retrieved from the database.
    	policies.forEach(policy -> policy.setRunId(run.getId()));
    	
    	PolicyRepository.updateOrInsertPolicies(transactionCtx, policies);
    	loadPersistedPolicies(transactionCtx, policies, run);

        List<Long> policyIds = policies.stream().mapToLong(Policy::getId).boxed().collect(Collectors.toList());

    	// Find max reward score iterations in DB, and calculate new reward scores to be inserted into db
    	Map<Long, Integer> maxRewardScoreIterations = RewardScoreRepository.getMaxRewardScoreIterationForPolicies(transactionCtx, policyIds);
    	Map<Long, List<RewardScore>> rewardScoresMap = new HashMap<>();
    	policies.forEach(policy -> {
    		Integer maxRewardScoreIteration = maxRewardScoreIterations.getOrDefault(policy.getId(), 0);
    		List<RewardScore> newRewardScores = policy.getScores().stream()
					.filter(score -> score.getIteration() > maxRewardScoreIteration)
					.collect(Collectors.toList());
			rewardScoresMap.put(policy.getId(), newRewardScores);
    	});

    	// Insert all new reward scores in a single batch
    	if (!rewardScoresMap.isEmpty()) {
    		RewardScoreRepository.insertRewardScores(transactionCtx, rewardScoresMap);
    	}

    	// Find max metric iteration in DB, and calculate new metrics to be inserted into db
        Map<Long, Integer> maxMetricsIterations = MetricsRepository.getMaxMetricsIterationForPolicies(transactionCtx, policyIds);
    	Map<Long, List<Metrics>> metricsMap = new HashMap<>();
    	policies.forEach(policy -> {
    	    Integer maxMetricsIteration = maxMetricsIterations.getOrDefault(policy.getId(), 0);
    	    if (policy.getMetrics() != null) {
                List<Metrics> newMetrics = policy.getMetrics().stream()
                    .filter(metrics -> metrics.getIteration() > maxMetricsIteration)
                    .collect(Collectors.toList());
                metricsMap.put(policy.getId(), newMetrics);
            }
        });

    	// Insert all new metrics in a single batch
        if (!metricsMap.isEmpty()) {
            MetricsRepository.insertMetrics(transactionCtx, metricsMap);
        }

        // Find max metric raw iteration in DB, and calculate new metrics to be inserted into db
        Map<Long, Integer> maxMetricsRawIterations = MetricsRawRepository.getMaxMetricsRawIterationForPolicies(transactionCtx, policyIds);
        Map<Long, List<MetricsRaw>> metricsRawMap = new HashMap<>();
        policies.forEach(policy -> {
            Integer maxMetricsRawIteration = maxMetricsRawIterations.getOrDefault(policy.getId(), 0);
            if (policy.getMetricsRaws() != null) {
                List<MetricsRaw> newMetricsRaw = policy.getMetricsRaws().stream()
                    .filter(metricsRaw -> metricsRaw.getIteration() > maxMetricsRawIteration)
                    .collect(Collectors.toList());
                metricsRawMap.put(policy.getId(), newMetricsRaw);
            }
        });

        // Insert all new metrics raw in a single batch
        if (!metricsRawMap.isEmpty()) {
            MetricsRawRepository.insertMetricsRaw(transactionCtx, metricsRawMap);
        }

    }

    public List<RewardScore> getScores(long runId, String policyExtId) {
        Policy policy = PolicyRepository.getPolicy(ctx, runId, policyExtId);

        if (policy == null) {
            return null;
        }

        return RewardScoreRepository.getRewardScoresForPolicy(ctx, policy.getId());
    }

    public List<Metrics> getMetrics(long runId, String policyExtId) {
        Policy policy = PolicyRepository.getPolicy(ctx, runId, policyExtId);

        if (policy == null) {
            return null;
        }

        return MetricsRepository.getMetricsForPolicy(ctx, policy.getId());
    }

    public List<MetricsRaw> getMetricsRaw(long runId, String policyExtId) {
        Policy policy = PolicyRepository.getPolicy(ctx, runId, policyExtId);

        if (policy == null) {
            return null;
        }

        return MetricsRawRepository.getMetricsRawForPolicy(ctx, policy.getId());
    }

    private void loadPersistedPolicies(DSLContext transactionCtx, List<Policy> policies, Run run) {
    	List<String> externalIds = policies.stream().map(Policy::getExternalId).collect(Collectors.toList());
		List<Policy> persistedPolicies = PolicyRepository.getPoliciesForRunAndExternalIds(transactionCtx, run.getId(), externalIds);
		policies.forEach(policy -> {
			persistedPolicies.stream()
				.filter(pp -> pp.getRunId() == policy.getRunId() && Objects.equals(pp.getExternalId(), policy.getExternalId()))
				.findAny()
				.ifPresent(pp -> {
					PolicyUtils.loadPolicyDataModel(policy, pp.getId(), run);
				});
		});
		
	}

    public List<String> unfinishedPolicyIds(long runId) {
        return PolicyRepository.getPoliciesForRun(ctx, runId).stream()
                .filter(p -> !p.hasFile())
                .map(Policy::getExternalId)
                .collect(Collectors.toList());
    }

    private void cleanUpInvalidPolicies(DSLContext transactionCtx, long runId, List<String> validExternalIds) {
        PolicyRepository.getPoliciesForRun(transactionCtx, runId).stream()
                .filter(p -> !validExternalIds.contains(p.getExternalId()))
                .forEach(p -> {
                    PolicyRepository.setIsValid(transactionCtx, p.getId(), false);
                    log.info(p.getExternalId() + " is marked as invalid");
                });
    }

    public List<Policy> updateRun(Run run, ProviderJobStatus providerJobStatus, List<Policy> policies,
            List<PolicyUpdateInfo> policiesUpdateInfo, List<String> validExternalIds) {
        return ctx.transactionResult(configuration -> {
            List<Policy> policiesToRaiseUpdateEvent = new ArrayList<>();
            DSLContext transactionCtx = DSL.using(configuration);

            DBUtils.setLockTimeout(transactionCtx, 4);

            updateRun(transactionCtx, run, providerJobStatus, policies);
            calculateGoals(transactionCtx, run.getExperiment(), policies);
            updateExperimentTrainingStatus(transactionCtx, run);

            if (RunStatus.isCompleting(providerJobStatus.getRunStatus())) {
            	policiesUpdateInfo.forEach(policyInfo -> {
            		Long policyId = PolicyRepository.getPolicyIdByRunIdAndExternalId(transactionCtx, run.getId(), policyInfo.getName());
            		PolicyRepository.setHasFileAndCheckPoint(transactionCtx, policyId, true, policyInfo.getCheckpointFileKey());
            		
            		Optional.ofNullable(getPolicy(transactionCtx, policyId)).ifPresent(policy -> {
            			policy.setHasFile(true);
            			policiesToRaiseUpdateEvent.add(policy);
            		});
            	});
            	
            	cleanUpInvalidPolicies(transactionCtx, run.getId(), validExternalIds);
            }
            return policiesToRaiseUpdateEvent;
        });
    }

    private void updateExperimentTrainingStatus(DSLContext transactionCtx, Run run) {
        Experiment experiment = run.getExperiment();
        List<Run> runsForExperiment = RunRepository.getRunsForExperiment(transactionCtx, experiment.getId());
        int currentIndex = runsForExperiment.indexOf(run);
        assert currentIndex != -1;
        runsForExperiment.set(currentIndex, run);
        experiment.setRuns(runsForExperiment);
        experiment.updateTrainingStatus();
        ExperimentRepository.updateTrainingStatus(transactionCtx, experiment);
    }

    private void calculateGoals(DSLContext transactionCtx, Experiment experiment, List<Policy> policies) {
        Policy bestPolicy = PolicyUtils.selectBestPolicy(policies);
        if (bestPolicy == null) {
            return;
        }
        List<RewardVariable> rewardVariables = RewardVariableRepository.getRewardVariablesForModel(transactionCtx, experiment.getModelId());
        PolicyUtils.updateSimulationMetricsData(bestPolicy);
        if (experiment.isHasGoals()) {
            boolean goalsReached = rewardVariables.stream()
                .filter(rv -> rv.getGoalConditionType() != null)
                .allMatch(rv -> PolicyUtils.isGoalReached(rv, bestPolicy));
            experiment.setGoalsReached(goalsReached);
            ExperimentRepository.updateGoalsReached(transactionCtx, experiment.getId(), goalsReached);
        }
    }

    public void updatePolicyData(Run run, List<Policy> policies) {
        ctx.transaction(configuration -> {
            DSLContext transactionCtx = DSL.using(configuration);
            updatePolicies(run, policies, transactionCtx);
        });
    }

    private Policy getPolicy(DSLContext transactionCtx, long policyId) {
        Policy policy = PolicyRepository.getPolicy(transactionCtx, policyId);
        policy.setScores(RewardScoreRepository.getRewardScoresForPolicy(transactionCtx, policyId));
        return policy;
    }

    /**
     * Gets the number of reward variable for AnyLogic Model for the given run id
     *
     * @param runId
     * @return
     */
    public int getRewardNumForRun(long runId) {
        return MetricsRepository.getRewardNumForRun(ctx, runId);
    }

    public UserMetrics getRunUsageDataForUser(long userId) {
        return RunRepository.getRunUsageDataForUser(ctx, userId);
    }

    public List<Policy> getPolicies(long runId) {
        return PolicyRepository.getPoliciesForRun(ctx, runId);
    }
}