package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.*;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.db.repositories.PolicyRepository;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.skymind.pathmind.data.db.Tables.*;

@Repository
public class PolicyDAO extends PolicyRepository
{
    private final DSLContext ctx;

    public PolicyDAO(DSLContext ctx){
        this.ctx = ctx;
    }

    public List<Policy> getPoliciesForExperiment(long experimentId){
        final List<Policy> policies = ctx.select(POLICY.ID, POLICY.EXTERNAL_ID, POLICY.NAME, POLICY.PROGRESS, POLICY.RUN_ID)
                .select(EXPERIMENT.asterisk())
                .select(RUN.asterisk())
                .select(MODEL.asterisk())
                .select(PROJECT.asterisk())
                .from(POLICY)
                .join(RUN)
                   .on(POLICY.RUN_ID.eq(RUN.ID))
                .join(EXPERIMENT)
                    .on(EXPERIMENT.ID.eq(RUN.EXPERIMENT_ID))
                .leftJoin(MODEL)
                    .on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
                .leftJoin(PROJECT)
                    .on(PROJECT.ID.eq(MODEL.PROJECT_ID))
                .where(RUN.EXPERIMENT_ID.eq(experimentId))
                .fetch(it -> {
                    final Policy policy = new Policy();
                    policy.setExternalId(it.get(POLICY.EXTERNAL_ID));
                    policy.setId(it.get(POLICY.ID));
                    policy.setName(it.get(POLICY.NAME));
                    policy.setRunId(it.get(POLICY.RUN_ID));

                    final JSONB progressJson = it.get(POLICY.PROGRESS);
                    policy.setProgress(progressJson.toString());
                    // TODO -> Although we process everything we could also get the values from the database. However until scores is also stored in the database
                    // we might as well do it here.
                    PolicyUtils.processProgressJson(policy);
                    // PERFORMANCE => can this be simplified? It's very expensive just to get Notes (both interpretKey and the HashMap of HyperParameters
                    PolicyUtils.getNotesFromName(policy);

                    policy.setRun(it.into(RUN).into(Run.class));
                    policy.setExperiment(it.into(EXPERIMENT).into(Experiment.class));
                    policy.setModel(it.into(MODEL).into(Model.class));
                    policy.setProject(it.into(PROJECT).into(Project.class));

                    // Helper for performance reasons
                    policy.setParsedName(PolicyUtils.parsePolicyName(policy.getName()));

                    return policy;
                });

        return policies;
    }

    public boolean hasPolicyFile(long policyId){
        return ctx.select(DSL.one()).from(POLICY).where(POLICY.ID.eq(policyId).and(POLICY.FILE.isNotNull())).fetchOptional().isPresent();
    }

    public byte[] getPolicyFile(long policyId){
        return ctx.select(POLICY.FILE).from(POLICY).where(POLICY.ID.eq(policyId).and(POLICY.FILE.isNotNull())).fetchOne(POLICY.FILE);
    }

    public long insertPolicy(Policy policy) {
        return ctx.insertInto(POLICY)
                .columns(POLICY.NAME, POLICY.RUN_ID, POLICY.EXTERNAL_ID)
                .values(policy.getName(), policy.getRunId(), policy.getName())
                .returning(POLICY.ID)
                .fetchOne()
                .getValue(POLICY.ID);
    }
}
