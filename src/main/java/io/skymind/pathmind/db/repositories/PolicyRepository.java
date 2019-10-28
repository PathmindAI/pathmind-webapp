package io.skymind.pathmind.db.repositories;

import io.skymind.pathmind.data.*;
import io.skymind.pathmind.data.utils.PolicyUtils;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static io.skymind.pathmind.data.db.Tables.*;

@Repository
public class PolicyRepository
{
    @Autowired
    private DSLContext dslContext;

    public List<Policy> getPoliciesForUser(long userId) {
        Result<?> result = dslContext
                .select(POLICY.asterisk())
                .select(RUN.ID, RUN.NAME, RUN.STATUS, RUN.RUN_TYPE, RUN.STARTED_AT, RUN.STOPPED_AT)
                .select(EXPERIMENT.ID, EXPERIMENT.NAME)
                .select(MODEL.ID, MODEL.NAME)
                .select(PROJECT.ID, PROJECT.NAME)
				.from(POLICY)
					.leftJoin(RUN)
						.on(RUN.ID.eq(POLICY.RUN_ID))
					.leftJoin(EXPERIMENT)
						.on(EXPERIMENT.ID.eq(RUN.EXPERIMENT_ID))
					.leftJoin(MODEL)
						.on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
					.leftJoin(PROJECT)
						.on(PROJECT.ID.eq(MODEL.PROJECT_ID))
					.leftJoin(PATHMIND_USER)
						.on(PATHMIND_USER.ID.eq(PROJECT.PATHMIND_USER_ID))
				.where(PATHMIND_USER.ID.eq(userId))
				.fetch();

		return result.stream().map(record -> {
        	Policy policy = record.into(POLICY).into(Policy.class);
			addParentDataModelObjects(record, policy);
			return policy;
		}).collect(Collectors.toList());
    }

	public Policy getPolicy(long policyId) {
		return getPolicy(dslContext, policyId);
	}

    public static Policy getPolicy(DSLContext ctx, long policyId) {
        Record record = ctx
                .select(POLICY.asterisk())
                .select(RUN.ID, RUN.NAME, RUN.STATUS, RUN.RUN_TYPE, RUN.STARTED_AT, RUN.STOPPED_AT)
                .select(EXPERIMENT.ID, EXPERIMENT.NAME)
                .select(MODEL.ID, MODEL.NAME)
                .select(PROJECT.ID, PROJECT.NAME)
				.from(POLICY)
					.leftJoin(RUN)
						.on(RUN.ID.eq(POLICY.RUN_ID))
					.leftJoin(EXPERIMENT)
						.on(EXPERIMENT.ID.eq(RUN.EXPERIMENT_ID))
					.leftJoin(MODEL)
						.on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
					.leftJoin(PROJECT)
						.on(PROJECT.ID.eq(MODEL.PROJECT_ID))
					.leftJoin(PATHMIND_USER)
						.on(PATHMIND_USER.ID.eq(PROJECT.PATHMIND_USER_ID))
				.where(POLICY.ID.eq(policyId))
				.fetchOne();

		Policy policy = record.into(POLICY).into(Policy.class);
		PolicyUtils.processProgressJson(policy);
		addParentDataModelObjects(record, policy);

		return policy;
    }

	private static void addParentDataModelObjects(Record record, Policy policy) {
		policy.setRun(record.into(RUN).into(Run.class));
		policy.setExperiment(record.into(EXPERIMENT).into(Experiment.class));
		policy.setModel(record.into(MODEL).into(Model.class));
		policy.setProject(record.into(PROJECT).into(Project.class));
	}
}
