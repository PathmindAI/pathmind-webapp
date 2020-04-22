package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.bus.EventBus;
import io.skymind.pathmind.shared.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.shared.bus.events.RunUpdateBusEvent;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.constants.RunType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.ProviderJobStatus;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.data.RewardScore;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class RunDAO
{
    private static Logger log = LoggerFactory.getLogger(RunDAO.class);

    private final DSLContext ctx;

    public RunDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public Run getRun(long runId) {
        return RunRepository.getRun(ctx, runId);
    }

    public List<Run> getRuns(List<Long> runIds) {
        return RunRepository.getRuns(ctx, runIds);
    }

    public List<Run> getRunsForExperiment(Experiment experiment) {
    	return RunRepository.getRunsForExperiment(ctx, experiment.getId());
    }

    public Run createRun(Experiment experiment, RunType runType){
        return RunRepository.createRun(ctx, experiment, runType);
    }

    public void markAsStarting(long runId){
        RunRepository.markAsStarting(ctx, runId);
    }

    /**
     * Returns true if
     * - there is no other run with same run type that still executing
     * - notification is not sent yet for any other run
     */
    public boolean shouldSendNotification(long experimentId, int runType) {
		return RunRepository.getAlreadyNotifiedOrStillExecutingRunsWithType(ctx, experimentId, runType).isEmpty();
	}

    public void markAsNotificationSent(long runId){
    	RunRepository.markAsNotificationSent(ctx, runId);
    }
    
    /**
     * This is used in case a run is restarted, so that Notification Sent value is cleared
     * and a notification can be sent again after the training is completed
     */
    public void clearNotificationSentInfo(long experimentId) {
    	RunRepository.clearNotificationSentInfo(ctx, experimentId);
    }

    public List<Long> getExecutingRuns() {
        return RunRepository.getExecutingRuns(ctx);
    }

    public Map<Long, List<String>> getStoppedPolicyNamesForRuns(List<Long> runIds) {
        return RunRepository.getStoppedPolicyNamesForRuns(ctx, runIds);
    }

    public void markAsStopping(Run run) {
    	updateRun(run, ProviderJobStatus.STOPPING, Collections.emptyList());
    }
    
    @Transactional
    public void updateRun(Run run, ProviderJobStatus status, List<Policy> policies)
    {
        ctx.transaction(configuration ->
        {
            DSLContext transactionCtx = DSL.using(configuration);

            updateRun(run, status, transactionCtx);
            updateExperiment(run, transactionCtx);
            if (!policies.isEmpty()) {
            	updatePolicies(run, policies, transactionCtx);
            }
        });

        // The EventBus updates have to be done AFTER the transaction is completed and NOT during in case the transaction fails.
        fireEventBusUpdates(run, policies);
    }

    private void fireEventBusUpdates(Run run, List<Policy> policies) {
        // An event for each policy since we only need to update some of the policies in a run.
    	if (!policies.isEmpty()) {
    		EventBus.post(new PolicyUpdateBusEvent(policies));
    	}
        // Send run updated event, meaning that all policies under the run is updated.
        // This is needed especially in dashboard, to refresh the item only once per run, instead of after all policy updates
        EventBus.post(new RunUpdateBusEvent(run));
    }

    private void updateExperiment(Run run, DSLContext transactionCtx) {
        ExperimentRepository.updateLastActivityDate(transactionCtx, run.getExperimentId());
    }

    private void updateRun(Run run, ProviderJobStatus jobStatus, DSLContext transactionCtx) {
        final var status = jobStatus.getRunStatus();
        // IMPORTANT -> Needed for both the updateStatus and EventBus post.
        run.setStatusEnum(status);

        // STEPH -> REFACTOR -> QUESTION -> Isn't this just a duplicate of setStoppedAtForFinishedPolicies()
        run.setStoppedAt(RunStatus.isRunning(status) ? null : LocalDateTime.now());
        RunRepository.updateStatus(transactionCtx, run);
    }

    private void updatePolicies(Run run, List<Policy> policies, DSLContext transactionCtx)
    {
    	// We need this line because the policies are generated from the progress string from the backend and are NOT retrieved from the database.
    	policies.forEach(policy -> policy.setRunId(run.getId()));
    	
    	PolicyRepository.updateOrInsertPolicies(transactionCtx, policies);
    	loadPersistedPolicies(transactionCtx, policies, run);
    	
        for (Policy policy : policies) {
            
            // Only insert new RewardScores
            int maxRewardScoreIteration = RewardScoreRepository.getMaxRewardScoreIteration(transactionCtx, policy.getId());
            if(maxRewardScoreIteration >= 0) {
                List<RewardScore> newRewardScores = policy.getScores().stream()
                        .filter(score -> score.getIteration() > maxRewardScoreIteration)
                        .collect(Collectors.toList());
                RewardScoreRepository.insertRewardScores(transactionCtx, policy.getId(), newRewardScores);
            }
        }
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

	public List<RewardScore> getScores(long runId, String policyExtId) {
        Policy policy =  PolicyRepository.getPolicy(ctx, runId, policyExtId);

        if (policy == null) {
            return null;
        }

        return RewardScoreRepository.getRewardScoresForPolicy(ctx, policy.getId());
    }
}