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
import java.util.Optional;
import java.util.stream.Collectors;

import static io.skymind.pathmind.data.db.Tables.*;

class PolicyRepository
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
                .select(POLICY.ID, POLICY.RUN_ID, POLICY.EXTERNAL_ID, POLICY.NAME, POLICY.PROGRESS, POLICY.STARTEDAT, POLICY.STOPPEDAT, POLICY.ALGORITHM, POLICY.LEARNING_RATE, POLICY.GAMMA, POLICY.BATCH_SIZE)
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
		addHyperParameters(record, policy);

		addParentDataModelObjects(record, policy);

		return policy;
    }

	private static void addParentDataModelObjects(Record record, Policy policy) {
		policy.setRun(record.into(RUN).into(Run.class));
		policy.setExperiment(record.into(EXPERIMENT).into(Experiment.class));
		policy.setModel(record.into(MODEL).into(Model.class));
		policy.setProject(record.into(PROJECT).into(Project.class));
	}

	// STEPH -> This should eventually be mapped into a RecordMapper but since there are other issues I cannot do that yet.
	// As for now I have to do a nullable check because when creating a new temp policy we do not seem to set the
	// initial values for these properties. The name seems to have the possibility of a list of these values but
	// after that it's always one.
	private static void addHyperParameters(Record record, Policy policy) {
		policy.getHyperParameters().setLearningRate(Optional.ofNullable(record.get(POLICY.LEARNING_RATE)).orElse(0.0));
		policy.getHyperParameters().setGamma(Optional.ofNullable(record.get(POLICY.GAMMA)).orElse(0.0));
		policy.getHyperParameters().setBatchSize(Optional.ofNullable(record.get(POLICY.BATCH_SIZE)).orElse(0));
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
				.columns(POLICY.NAME, POLICY.RUN_ID, POLICY.EXTERNAL_ID, POLICY.ALGORITHM, POLICY.LEARNING_RATE, POLICY.GAMMA, POLICY.BATCH_SIZE)
				.values(policy.getName(), policy.getRunId(), policy.getName(), policy.getAlgorithm(), policy.getHyperParameters().getLearningRate(), policy.getHyperParameters().getGamma(), policy.getHyperParameters().getBatchSize())
				.returning(POLICY.ID)
				.fetchOne()
				.getValue(POLICY.ID);
	}

	protected static List<Policy> getPoliciesForExperiment(DSLContext ctx, long experimentId) {
		final List<Policy> policies = ctx.select(POLICY.ID, POLICY.EXTERNAL_ID, POLICY.NAME, POLICY.PROGRESS, POLICY.RUN_ID, POLICY.LEARNING_RATE, POLICY.GAMMA, POLICY.BATCH_SIZE)
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
					addHyperParameters(record, policy);
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
				.columns(POLICY.NAME, POLICY.RUN_ID, POLICY.EXTERNAL_ID, POLICY.PROGRESS, POLICY.STARTEDAT, POLICY.STOPPEDAT, POLICY.ALGORITHM, POLICY.LEARNING_RATE, POLICY.GAMMA, POLICY.BATCH_SIZE)
				.values(policy.getExternalId(), policy.getRunId(), policy.getExternalId(), progressJson, policy.getStartedAt(), policy.getStoppedAt(), policy.getAlgorithm(), policy.getHyperParameters().getLearningRate(), policy.getHyperParameters().getGamma(), policy.getHyperParameters().getBatchSize())
				.onConflict(POLICY.RUN_ID, POLICY.EXTERNAL_ID)
				.doUpdate()
				.set(POLICY.PROGRESS, progressJson)
				.set(POLICY.STARTEDAT, policy.getStartedAt())
				.set(POLICY.STOPPEDAT, policy.getStoppedAt())
				.returning(POLICY.ID)
				.fetchOne()
				.getValue(POLICY.ID);
	}

	protected static boolean isTemporaryPolicy(DSLContext ctx, long runId, String tempKeyword) {
		return ctx.select(DSL.one())
				.from(POLICY)
				.where(POLICY.RUN_ID.eq(runId)
						.and(POLICY.EXTERNAL_ID.like("%" + tempKeyword)))
				.fetchOptional().isPresent();
	}

	protected static void deleteTemporaryPolicy(DSLContext ctx, long runId, String tempKeyword) {
		ctx.delete(POLICY)
				.where(POLICY.RUN_ID.eq(runId).and(POLICY.EXTERNAL_ID.like("%" + tempKeyword)))
				.execute();
	}
}
