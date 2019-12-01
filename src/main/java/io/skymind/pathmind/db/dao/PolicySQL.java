package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.*;
import io.skymind.pathmind.data.utils.PolicyUtils;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static io.skymind.pathmind.data.db.Tables.*;

@Repository
class PolicySQL
{
	private static final String SAVING = "saving";

	protected static List<Policy> getActivePoliciesForUser(DSLContext ctx, long userId) {
        Result<?> result = ctx
                .select(POLICY.ID, POLICY.RUN_ID, POLICY.EXTERNAL_ID, POLICY.NAME, POLICY.PROGRESS, POLICY.STARTEDAT, POLICY.STOPPEDAT, POLICY.ALGORITHM)
                .select(RUN.ID, RUN.NAME, RUN.STATUS, RUN.RUN_TYPE, RUN.STARTED_AT, RUN.STOPPED_AT)
                .select(EXPERIMENT.ID, EXPERIMENT.NAME)
                .select(MODEL.ID, MODEL.NAME)
                .select(PROJECT.ID, PROJECT.NAME)
				.from(POLICY)
					.leftJoin(RUN).on(RUN.ID.eq(POLICY.RUN_ID))
					.leftJoin(EXPERIMENT).on(EXPERIMENT.ID.eq(RUN.EXPERIMENT_ID))
					.leftJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
					.leftJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
					.leftJoin(PATHMIND_USER).on(PATHMIND_USER.ID.eq(PROJECT.PATHMIND_USER_ID))
				.where(PATHMIND_USER.ID.eq(userId)
						.and(PROJECT.ARCHIVED.isFalse())
						.and(MODEL.ARCHIVED.isFalse())
						.and(EXPERIMENT.ARCHIVED.isFalse()))
				.fetch();

		return result.stream().map(record -> {
        	Policy policy = record.into(POLICY).into(Policy.class);
			addParentDataModelObjects(record, policy);
			return policy;
		}).collect(Collectors.toList());
    }

	protected static Policy getPolicy(DSLContext ctx, long policyId) {
        Record record = ctx
                .select(POLICY.ID, POLICY.RUN_ID, POLICY.EXTERNAL_ID, POLICY.NAME, POLICY.PROGRESS, POLICY.STARTEDAT, POLICY.STOPPEDAT, POLICY.ALGORITHM)
                .select(RUN.ID, RUN.NAME, RUN.STATUS, RUN.RUN_TYPE, RUN.STARTED_AT, RUN.STOPPED_AT)
                .select(EXPERIMENT.ID, EXPERIMENT.NAME)
                .select(MODEL.ID, MODEL.NAME)
                .select(PROJECT.ID, PROJECT.NAME)
				.from(POLICY)
					.leftJoin(RUN).on(RUN.ID.eq(POLICY.RUN_ID))
					.leftJoin(EXPERIMENT).on(EXPERIMENT.ID.eq(RUN.EXPERIMENT_ID))
					.leftJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
					.leftJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
					.leftJoin(PATHMIND_USER).on(PATHMIND_USER.ID.eq(PROJECT.PATHMIND_USER_ID))
				.where(POLICY.ID.eq(policyId))
				.fetchOne();

		Policy policy = record.into(POLICY).into(Policy.class);

		// PERFORMANCE -> Until we remove the json progress string this is to help optimizing the memory usage.
		PolicyUtils.processProgressJson(policy, policy.getProgress());
		policy.setProgress(null);

		policy.setParsedName(PolicyUtils.parsePolicyName(policy.getName()));
		// STEPH -> This is very expensive for what it does but before it was masked under a different stack of code. Once
		// the HyperParameters are moved into the database we can delete this code.
		policy.setHyperParameters(PolicyUtils.getHyperParametersFromName(policy));

		addParentDataModelObjects(record, policy);

		return policy;
    }

	private static void addParentDataModelObjects(Record record, Policy policy) {
		policy.setRun(record.into(RUN).into(Run.class));
		policy.setExperiment(record.into(EXPERIMENT).into(Experiment.class));
		policy.setModel(record.into(MODEL).into(Model.class));
		policy.setProject(record.into(PROJECT).into(Project.class));
	}

	protected static boolean hasPolicyFile(DSLContext ctx, long policyId) {
		return ctx.select(DSL.one())
				.from(POLICY)
				.where(POLICY.ID.eq(policyId)
						.and(POLICY.FILE.isNotNull())
						.and(POLICY.FILE.notEqual(SAVING.getBytes())))
				.fetchOptional().isPresent();
	}

	protected static byte[] getPolicyFile(DSLContext ctx, long policyId) {
		return ctx.select(POLICY.FILE)
				.from(POLICY)
				.where(POLICY.ID.eq(policyId)
						.and(POLICY.FILE.isNotNull()))
				.fetchOne(POLICY.FILE);
	}

	protected static long insertPolicy(DSLContext ctx, Policy policy) {
		return ctx.insertInto(POLICY)
				.columns(POLICY.NAME, POLICY.RUN_ID, POLICY.EXTERNAL_ID, POLICY.ALGORITHM)
				.values(policy.getName(), policy.getRunId(), policy.getName(), policy.getAlgorithm())
				.returning(POLICY.ID)
				.fetchOne()
				.getValue(POLICY.ID);
	}

	protected static List<Policy> getPoliciesForExperiment(DSLContext ctx, long experimentId) {
		final List<Policy> policies = ctx.select(POLICY.ID, POLICY.EXTERNAL_ID, POLICY.NAME, POLICY.PROGRESS, POLICY.RUN_ID)
				.select(EXPERIMENT.asterisk())
				.select(RUN.asterisk())
				.select(MODEL.ID, MODEL.PROJECT_ID, MODEL.NAME, MODEL.DATE_CREATED, MODEL.LAST_ACTIVITY_DATE, MODEL.NUMBER_OF_OBSERVATIONS, MODEL.NUMBER_OF_POSSIBLE_ACTIONS, MODEL.GET_OBSERVATION_FOR_REWARD_FUNCTION, MODEL.ARCHIVED)
				.select(PROJECT.asterisk())
				.from(POLICY)
				.leftJoin(RUN).on(POLICY.RUN_ID.eq(RUN.ID))
				.leftJoin(EXPERIMENT).on(EXPERIMENT.ID.eq(RUN.EXPERIMENT_ID))
				.leftJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
				.leftJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
				.where(RUN.EXPERIMENT_ID.eq(experimentId))
				.orderBy(POLICY.ID)
				.fetch(record -> {
					final Policy policy = new Policy();
					policy.setExternalId(record.get(POLICY.EXTERNAL_ID));
					policy.setId(record.get(POLICY.ID));
					policy.setName(record.get(POLICY.NAME));
					policy.setRunId(record.get(POLICY.RUN_ID));

					// TODO -> Although we process everything we could also get the values from the database. However until scores is also stored in the database
					// we might as well do it here.
					// PERFORMANCE => can this be simplified? It's very expensive just to get Notes (both interpretKey and the HashMap of HyperParameters
					PolicyUtils.processProgressJson(policy, record.get(POLICY.PROGRESS).toString());
					// STEPH -> This is very expensive for what it does but before it was masked under a different stack of code. Once
					// the HyperParameters are moved into the database we can delete this code.
					policy.setHyperParameters(PolicyUtils.getHyperParametersFromName(policy));
					policy.setProgress(null);

					addParentDataModelObjects(record, policy);

					// Helper for performance reasons
					policy.setParsedName(PolicyUtils.parsePolicyName(policy.getName()));

					return policy;
				});

		return policies;
	}

	protected static void updatePolicyNameAndExternalId(DSLContext ctx, long runId, String newExternalId, String oldExternalId) {
		ctx.update(POLICY)
				.set(POLICY.NAME, newExternalId)
				.set(POLICY.EXTERNAL_ID, newExternalId)
				.where(POLICY.RUN_ID.eq(runId), POLICY.EXTERNAL_ID.eq(oldExternalId))
				.execute();
	}

	// STEPH -> Still passing progressJSon as a temporary solution until I have the time to completely replace it and put the data in the database.
	protected static long updateOrInsertPolicy(DSLContext ctx, Policy policy, JSONB progressJson)
	{
		return ctx.insertInto(POLICY)
				.columns(POLICY.NAME, POLICY.RUN_ID, POLICY.EXTERNAL_ID, POLICY.PROGRESS, POLICY.STARTEDAT, POLICY.STOPPEDAT, POLICY.ALGORITHM)
				.values(policy.getExternalId(), policy.getRunId(), policy.getExternalId(), progressJson, policy.getStartedAt(), policy.getStoppedAt(), policy.getAlgorithm())
				.onConflict(POLICY.RUN_ID, POLICY.EXTERNAL_ID)
				.doUpdate()
				.set(POLICY.PROGRESS, progressJson)
				.set(POLICY.STARTEDAT, policy.getStartedAt())
				.set(POLICY.STOPPEDAT, policy.getStoppedAt())
				.returning(POLICY.ID)
				.fetchOne()
				.getValue(POLICY.ID);
	}
}
