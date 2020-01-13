package io.skymind.pathmind.db.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.skymind.pathmind.bus.EventBus;
import io.skymind.pathmind.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.bus.events.RunUpdateBusEvent;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.policy.RewardScore;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.data.utils.RunUtils;

@Repository
public class RunDAO
{
    private static Logger log = LoggerFactory.getLogger(RunDAO.class);

    private final DSLContext ctx;
    private final ObjectMapper mapper;

    public RunDAO(DSLContext ctx, ObjectMapper mapper) {
        this.ctx = ctx;
        this.mapper = mapper;
    }

    public Run getRun(long runId) {
        return RunRepository.getRun(ctx, runId);
    }

    public List<Run> getRuns(List<Long> runIds) {
        return RunRepository.getRuns(ctx, runIds);
    }

    public Run createRun(Experiment experiment, RunType runType){
        return RunRepository.createRun(ctx, experiment, runType);
    }

    public void markAsStarting(long runId){
        RunRepository.markAsStarting(ctx, runId);
    }

    public List<Run> getRunsForExperiment(long experimentId) {
        return RunRepository.getRunsForExperiment(ctx, experimentId);
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

    public List<Long> getExecutingRuns() {
        return RunRepository.getExecutingRuns(ctx);
    }

    public void savePolicyFile(long runId, String externalId, byte[] policyFile) {
        RunRepository.savePolicyFile(ctx, runId, externalId, policyFile);
    }

    public void saveCheckpointFile(long runId, String externalId, byte[] checkpointFile) {
        RunRepository.saveCheckpointFile(ctx, runId, externalId, checkpointFile);
    }

    public Map<Long, List<String>> getStoppedPolicyNamesForRuns(List<Long> runIds) {
        return RunRepository.getStoppedPolicyNamesForRuns(ctx, runIds);
    }

    @Transactional
    public void updateRun(Run run, RunStatus status, List<Policy> policies)
    {
        ctx.transaction(configuration ->
        {
            DSLContext transactionCtx = DSL.using(configuration);

            updateRun(run, status, transactionCtx);
            updateExperiment(run, transactionCtx);
            updateFirstPolicy(run, policies, transactionCtx);
            updatePolicies(run, policies, transactionCtx);
        });

        // The EventBus updates have to be done AFTER the transaction is completed and NOT during in case the transaction fails.
        fireEventBusUpdates(run, policies);
    }

    private void fireEventBusUpdates(Run run, List<Policy> policies) {
        // An event for each policy since we only need to update some of the policies in a run.
        policies.stream().forEach(policy -> EventBus.post(new PolicyUpdateBusEvent(policy)));
        // This is only needed because if the run is completed then with the current API there are no policies
        // to update. By updating the run we can then let all the interested policies on screen know to update themselves.
        if(policies.isEmpty())
            EventBus.post(new RunUpdateBusEvent(run));
    }

    // STEPH -> REFACTOR -> QUESTION -> Rename method with an explanation of what this does? It generates a temp policy name but why? And why get only index 0.
    private void updateFirstPolicy(Run run, List<Policy> policies, DSLContext transactionCtx) {
        // IMPORTANT -> Keep the policies.size() check  first because if it fails then we can avoid the database call.
        if (policies.size() > 0 && PolicyRepository.isTemporaryPolicy(ctx, run.getId(), RunUtils.TEMPORARY_POSTFIX)) {
            Policy policy = policies.get(0);
            //PPO_PathmindEnvironment_0_gamma=0.99,lr=1e-05,sgd_minibatch_size=128_1TEMP
            PolicyRepository.updatePolicyExternalId(transactionCtx, run.getId(), policy.getExternalId(), PolicyUtils.generatePolicyTempName(policy.getExternalId(), run.getRunType()));
        }
    }

    private void updateExperiment(Run run, DSLContext transactionCtx) {
        ExperimentRepository.updateLastActivityDate(transactionCtx, run.getExperimentId());
    }

    private void updateRun(Run run, RunStatus status, DSLContext transactionCtx) {
        // IMPORTANT -> Needed for both the updateStatus and EventBus post.
        run.setStatusEnum(status);
        // STEPH -> REFACTOR -> QUESTION -> Isn't this just a duplicate of setStoppedAtForFinishedPolicies()
        run.setStoppedAt(RunStatus.isRunning(status) ? null : LocalDateTime.now());
        RunRepository.updateStatus(transactionCtx, run);
    }

    private void updatePolicies(Run run, List<Policy> policies, DSLContext transactionCtx)
    {
        for (Policy policy : policies) {
            try {
                // We need this line because the policies are generated from the progress string from the backend and are NOT retrieved from the database.
                policy.setRunId(run.getId());

                // STEPH -> REFACTOR -> PERFORMANCE -> We should store these values in the database rather than having to parse JSON all the time. The more
                // entries there are in the progress the more expensive (memory/cpu) this becomes, especially for full runs when all we need is the
                // latest entries.
                final String progressJsonStr = mapper.writeValueAsString(policy);
                final JSONB progressJson = JSONB.valueOf(progressJsonStr);

                // STEPH -> REFACTOR -> If there are multiple policies why don't we just update the last policy in the list. This is no worse
                // than what we have now. Plus this is a loop within a loop of database inserts and other database calls. We should instead
                // update ONLY the most recent policy and ignore the others since really all we're doing is updating to get the progress.
                // IMPORTANT -> The most recent policy may NOT be the last policy in the list.
                long policyId = PolicyRepository.updateOrInsertPolicy(transactionCtx, policy, progressJson);

                // Load up the Policy object so that we can push it to the GUI for the event. We may not need everything in here any more...
                PolicyUtils.loadPolicyDataModel(policy, policyId, run);
                // STEPH -> REFACTOR -> Now that it's transactional is it ok to post to the eventbus? For now this is no worse then what we are doing in production today
                // but we should look at putting this after the transaction has been completed. The only issue is that it's a list of policies that may need
                // to be updated. As in this should technically be placed outside of the transaction in updateRun() rather than here. Again it's no worse then what we
                // have in place today, and if I'm correct the worse place is that we'd have an update that isn't legit and it would be corrected on the next update anyways.
                EventBus.post(new PolicyUpdateBusEvent(policy));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

    // STEPH -> REFACTOR -> Can we just make this as a single SQL call with a WHERE?. This should be in the updatePolicies method rather than in the service
    // to make it all transactional. I will do this as another github issue in another PR.
    public void cleanUpTemporary(long runId) {
        boolean isExist = PolicyRepository.isTemporaryPolicy(ctx, runId, RunUtils.TEMPORARY_POSTFIX);
        if (isExist) {
            PolicyRepository.deleteTemporaryPolicy(ctx, runId, RunUtils.TEMPORARY_POSTFIX);
            log.info("Cleaned Temporary Policies in " + runId);
        }
    }

    public List<RewardScore> getScores(long runId, String policyExtId) {
        Policy policy =  PolicyRepository.getPolicy(ctx, runId, policyExtId);

        // check temporary policy
        if (policy == null) {
            int runType = RunRepository.getRunType(ctx, runId);
            policy = PolicyRepository.getPolicy(ctx, runId, PolicyUtils.generatePolicyTempName(policyExtId, runType));
        }

        if (policy == null) {
            return null;
        }

        PolicyUtils.processProgressJson(policy, policy.getProgress());

        return policy.getScores();
    }


}