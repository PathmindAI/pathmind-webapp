package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.constants.RunType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.PolicyUpdateInfo;
import io.skymind.pathmind.shared.data.ProviderJobStatus;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.data.RewardScore;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class RunDAO {

    private final DSLContext ctx;

    private final ExecutionProviderMetaDataDAO executionProviderMetaDataDAO;

    public RunDAO(DSLContext ctx, ExecutionProviderMetaDataDAO executionProviderMetaDataDAO) {
        this.ctx = ctx;
        this.executionProviderMetaDataDAO = executionProviderMetaDataDAO;
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

    public Run createRun(Experiment experiment, RunType runType) {
        return RunRepository.createRun(ctx, experiment, runType);
    }

    public void markAsStarting(long runId) {
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

    public List<Long> getExecutingRuns() {
        return RunRepository.getExecutingRuns(ctx);
    }

    public Map<Long, List<String>> getStoppedPolicyNamesForRuns(List<Long> runIds) {
        return RunRepository.getStoppedPolicyNamesForRuns(ctx, runIds);
    }

    public void updateRun(Run run, ProviderJobStatus status, List<Policy> policies) {
        ctx.transaction(configuration ->
        {
            DSLContext transactionCtx = DSL.using(configuration);
            updateRun(transactionCtx, run, status, policies);
        });
    }

    private void updateRun(DSLContext transactionCtx, Run run, ProviderJobStatus status, List<Policy> policies) {
        updateRun(run, status, transactionCtx);
        updateExperiment(run, transactionCtx);
        updatePolicies(run, policies, transactionCtx);
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

    private void updatePolicies(Run run, List<Policy> policies, DSLContext transactionCtx) {
        for (Policy policy : policies) {
            // We need this line because the policies are generated from the progress string from the backend and are NOT retrieved from the database.
            policy.setRunId(run.getId());

            // STEPH -> REFACTOR -> If there are multiple policies why don't we just update the last policy in the list. This is no worse
            // than what we have now. Plus this is a loop within a loop of database inserts and other database calls. We should instead
            // update ONLY the most recent policy and ignore the others since really all we're doing is updating to get the progress.
            // IMPORTANT -> The most recent policy may NOT be the last policy in the list.
            long policyId = PolicyRepository.updateOrInsertPolicy(transactionCtx, policy);
            // Load up the Policy object so that we can push it to the GUI for the event. We may not need everything in here any more...
            PolicyUtils.loadPolicyDataModel(policy, policyId, run);

            // Only insert new RewardScores
            int maxRewardScoreIteration = RewardScoreRepository
                    .getMaxRewardScoreIteration(transactionCtx, policy.getId());
            if (maxRewardScoreIteration >= 0) {
                List<RewardScore> newRewardScores = policy.getScores().stream()
                        .filter(score -> score.getIteration() > maxRewardScoreIteration)
                        .collect(Collectors.toList());
                RewardScoreRepository.insertRewardScores(transactionCtx, policy.getId(), newRewardScores);
            }
        }
    }

    public List<RewardScore> getScores(long runId, String policyExtId) {
        Policy policy = PolicyRepository.getPolicy(ctx, runId, policyExtId);

        if (policy == null) {
            return null;
        }

        return RewardScoreRepository.getRewardScoresForPolicy(ctx, policy.getId());
    }

    public List<String> unfinishedPolicyIds(long runId) {
        return PolicyRepository.getPoliciesForRun(ctx, runId).stream()
                .filter(p -> !p.hasFile())
                .map(Policy::getExternalId)
                .collect(Collectors.toList());
    }

    private void cleanUpInvalidPolicies(DSLContext transactionCtx, long runId, List<String> validExternalIds) {
        PolicyRepository.getPoliciesForRun(ctx, runId).stream()
                .filter(p -> !validExternalIds.contains(p.getExternalId()))
                .forEach(p -> {
                    PolicyRepository.setIsValid(ctx, p.getId(), false);
                    log.info(p.getExternalId() + " is marked as invalid");
                });
    }

    public List<Policy> updateRun(Run run, ProviderJobStatus providerJobStatus, List<Policy> policies,
            List<PolicyUpdateInfo> policiesUpdateInfo, List<String> validExternalIds) {
        return ctx.transactionResult(configuration -> {
            List<Policy> policiesToRaiseUpdateEvent = new ArrayList<>();
            DSLContext transactionCtx = DSL.using(configuration);
            updateRun(transactionCtx, run, providerJobStatus, policies);

            policiesUpdateInfo.forEach(policyInfo -> {
                Long policyId = PolicyRepository
                        .getPolicyIdByRunIdAndExternalId(transactionCtx, run.getId(), policyInfo.getName());
                PolicyRepository.setHasFile(transactionCtx, policyId, true);

                Optional.ofNullable(getPolicy(transactionCtx, policyId))
                        .ifPresent(policy -> {
                            policy.setHasFile(true);
                            policiesToRaiseUpdateEvent.add(policy);
                        });

                if (policyInfo.getCheckpointFile() != null) {
                    // save meta data for checkpoint
                    if (executionProviderMetaDataDAO.getCheckPointFileKey(transactionCtx, policyInfo.getName())
                            == null) {
                        executionProviderMetaDataDAO
                                .putCheckPointFileKey(transactionCtx, policyInfo.getName(),
                                        policyInfo.getCheckpointFileKey());
                    }
                }
                cleanUpInvalidPolicies(transactionCtx, run.getId(), validExternalIds);
            });
            return policiesToRaiseUpdateEvent;
        });
    }

    private Policy getPolicy(DSLContext transactionCtx, long policyId) {
        Policy policy = PolicyRepository.getPolicy(transactionCtx, policyId);
        policy.setScores(RewardScoreRepository.getRewardScoresForPolicy(transactionCtx, policyId));
        return policy;
    }
}