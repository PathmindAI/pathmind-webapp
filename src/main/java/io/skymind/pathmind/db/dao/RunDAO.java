package io.skymind.pathmind.db.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.bus.EventBus;
import io.skymind.pathmind.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.utils.PolicyUtils;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
        return RunSQL.getRun(ctx, runId);
    }

    public List<Run> getRuns(List<Long> runIds) {
        return RunSQL.getRuns(ctx, runIds);
    }

    public Run createRun(Experiment experiment, RunType runType){
        return RunSQL.createRun(ctx, experiment, runType);
    }

    public void markAsStarting(long runId){
        RunSQL.markAsStarting(ctx, runId);
    }

    public List<Run> getRunsForExperiment(long experimentId) {
        return RunSQL.getRunsForExperiment(ctx, experimentId);
    }

    public List<Long> getExecutingRuns() {
        return RunSQL.getExecutingRuns(ctx);
    }

    public void savePolicyFile(long runId, String externalId, byte[] policyFile) {
        RunSQL.savePolicyFile(ctx, runId, externalId, policyFile);
    }

    public Map<Long, List<String>> getStoppedPolicyNamesForRuns(List<Long> runIds) {
        return RunSQL.getStoppedPolicyNamesForRuns(ctx, runIds);
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
    }

    // STEPH -> REFACTOR -> QUESTION -> Rename method with an explanation of what this does? It generates a temp policy name but why? And why get only index 0.
    private void updateFirstPolicy(Run run, List<Policy> policies, DSLContext transactionCtx) {
        if (policies.size() > 0) {
            Policy policy = policies.get(0);
            //PPO_PathmindEnvironment_0_gamma=0.99,lr=1e-05,sgd_minibatch_size=128_1TEMP
            PolicySQL.updatePolicyNameAndExternalId(transactionCtx, run.getId(), policy.getExternalId(), PolicyUtils.generatePolicyTempName(policy, run));
        }
    }

    private void updateExperiment(Run run, DSLContext transactionCtx) {
        ExperimentSQL.updateLastActivityDate(transactionCtx, run.getExperimentId());
    }

    private void updateRun(Run run, RunStatus status, DSLContext transactionCtx) {
        // IMPORTANT -> Needed for both the updateStatus and EventBus post.
        run.setStatusEnum(status);
        // STEPH -> REFACTOR -> QUESTION -> Isn't this just a duplicate of setStoppedAtForFinishedPolicies()
        run.setStoppedAt(RunStatus.isRunning(status) ? null : LocalDateTime.now());
        RunSQL.updateStatus(transactionCtx, run);
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
                long policyId = PolicySQL.updateOrInsertPolicy(transactionCtx, policy, progressJson);

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
}
