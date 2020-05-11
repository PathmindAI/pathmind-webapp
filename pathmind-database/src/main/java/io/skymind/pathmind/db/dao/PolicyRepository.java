package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.data.Run;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Result;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static io.skymind.pathmind.db.jooq.Tables.*;

class PolicyRepository {

	protected static List<Policy> getActivePoliciesForUser(DSLContext ctx, long userId) {
        Result<?> result = ctx
                .select(POLICY.ID, POLICY.RUN_ID, POLICY.EXTERNAL_ID, POLICY.NAME, POLICY.STARTED_AT, POLICY.STOPPED_AT, POLICY.HAS_FILE)
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
				.select(POLICY.ID, POLICY.RUN_ID, POLICY.EXTERNAL_ID, POLICY.NAME, POLICY.STARTED_AT, POLICY.STOPPED_AT, POLICY.HAS_FILE)
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
		addParentDataModelObjects(record, policy);
		return policy;
    }

    protected static Policy getPolicy(DSLContext ctx, long runId, String policyExternalId) {
        return ctx.selectFrom(POLICY)
                .where(POLICY.RUN_ID.eq(runId).and(POLICY.EXTERNAL_ID.in(policyExternalId)))
                .fetchOneInto(Policy.class);
    }

	private static void addParentDataModelObjects(Record record, Policy policy) {
		policy.setRun(record.into(RUN).into(Run.class));
		policy.setExperiment(record.into(EXPERIMENT).into(Experiment.class));
		policy.setModel(record.into(MODEL).into(Model.class));
		policy.setProject(record.into(PROJECT).into(Project.class));
	}

	protected static long insertPolicy(DSLContext ctx, Policy policy) {
		return ctx.insertInto(POLICY)
				.columns(POLICY.NAME, POLICY.RUN_ID, POLICY.EXTERNAL_ID)
				.values(policy.getName(), policy.getRunId(), policy.getExternalId())
				.returning(POLICY.ID)
				.fetchOne()
				.getValue(POLICY.ID);
	}

	protected static List<Policy> getPoliciesForExperiment(DSLContext ctx, long experimentId) {
		final List<Policy> policies = ctx.select(POLICY.ID, POLICY.EXTERNAL_ID, POLICY.NAME, POLICY.RUN_ID, POLICY.STARTED_AT, POLICY.STOPPED_AT, POLICY.HAS_FILE)
				.select(EXPERIMENT.asterisk())
				.select(RUN.asterisk())
				.select(MODEL.ID, MODEL.PROJECT_ID, MODEL.NAME, MODEL.DATE_CREATED, MODEL.LAST_ACTIVITY_DATE, MODEL.NUMBER_OF_OBSERVATIONS, MODEL.NUMBER_OF_POSSIBLE_ACTIONS, MODEL.ARCHIVED, MODEL.USER_NOTES)
				.select(PROJECT.asterisk())
				.from(POLICY)
				.leftJoin(RUN).on(POLICY.RUN_ID.eq(RUN.ID))
				.leftJoin(EXPERIMENT).on(EXPERIMENT.ID.eq(RUN.EXPERIMENT_ID))
				.leftJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
				.leftJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
				.where(RUN.EXPERIMENT_ID.eq(experimentId))
				.orderBy(POLICY.ID)
				.fetch(record -> {
					Policy policy = record.into(POLICY).into(Policy.class);
					addParentDataModelObjects(record, policy);
					return policy;
				});

		return policies;
	}
	
	protected static List<Policy> getPoliciesForRunAndExternalIds(DSLContext ctx, long runId, List<String> externalIdList) {
		final List<Policy> policies = ctx.select(POLICY.ID, POLICY.EXTERNAL_ID, POLICY.NAME, POLICY.RUN_ID, POLICY.STARTED_AT, POLICY.STOPPED_AT, POLICY.HAS_FILE)
				.from(POLICY)
				.where(POLICY.RUN_ID.eq(runId))
				.and(POLICY.EXTERNAL_ID.in(externalIdList))
				.orderBy(POLICY.ID)
				.fetch(record -> record.into(POLICY).into(Policy.class));
		
		return policies;
	}

	protected static List<Policy> getPoliciesForRun(DSLContext ctx, long runId) {
		final List<Policy> policies = ctx.select(POLICY.asterisk())
				.from(POLICY)
				.where(POLICY.RUN_ID.eq(runId))
				.fetch(record -> record.into(POLICY).into(Policy.class));
		return policies;
	}

	protected static void updatePolicyExternalId(DSLContext ctx, long runId, String newExternalId, String oldExternalId) {
		ctx.update(POLICY)
				.set(POLICY.EXTERNAL_ID, newExternalId)
				.where(POLICY.RUN_ID.eq(runId), POLICY.EXTERNAL_ID.eq(oldExternalId))
				.execute();
	}

	protected static void updateOrInsertPolicies(DSLContext ctx, List<Policy> policies) {
		final List<Query> saveQueries = policies.stream()
                .map(policy ->
                		ctx.insertInto(POLICY)
                			.columns(POLICY.NAME, POLICY.RUN_ID, POLICY.EXTERNAL_ID, POLICY.STARTED_AT, POLICY.STOPPED_AT)
                			.values(policy.getName(), policy.getRunId(), policy.getExternalId(), policy.getStartedAt(), policy.getStoppedAt())
                			.onConflict(POLICY.RUN_ID, POLICY.EXTERNAL_ID)
                			.doUpdate()
                			.set(POLICY.STARTED_AT, policy.getStartedAt())
                			.set(POLICY.STOPPED_AT, policy.getStoppedAt()))
                .collect(Collectors.toList());

        ctx.batch(saveQueries).execute();
	}

	public static List<Policy> getExportedPoliciesByRunId(DSLContext ctx, long runId) {
		return ctx.select(POLICY.ID, POLICY.RUN_ID, POLICY.EXTERNAL_ID, POLICY.NAME, POLICY.STARTED_AT,
				POLICY.STOPPED_AT, POLICY.EXPORTED_AT)
				.from(POLICY)
				.where(POLICY.RUN_ID.eq(runId))
				.and(POLICY.EXPORTED_AT.isNotNull())
				.fetch(record -> record.into(POLICY).into(Policy.class));
	}

	static void updateExportedDate(DSLContext ctx, long policyId) {
		ctx.update(POLICY)
				.set(POLICY.EXPORTED_AT, LocalDateTime.now())
				.where(POLICY.ID.eq(policyId))
				.execute();

	}

	static Long getPolicyIdByRunIdAndExternalId(DSLContext ctx, long runId, String externalId) {
		return ctx.select(POLICY.ID).from(POLICY)
				.where(
						POLICY.EXTERNAL_ID.eq(externalId).and(POLICY.RUN_ID.eq(runId))
				).fetchOne(POLICY.ID);
	}

	public static void setHasFileAndCheckPoint(DSLContext ctx, Long policyId, boolean hasFile, String checkPointFileKey) {
 		ctx.update(POLICY)
 			.set(POLICY.HAS_FILE, hasFile)
 			.set(POLICY.CHECK_POINT_FILE_KEY, checkPointFileKey)
 			.where(POLICY.ID.eq(policyId)).execute();
 	}

	public static void setIsValid(DSLContext ctx, long policyId, boolean value) {
		ctx.update(POLICY).set(POLICY.IS_VALID, value).where(POLICY.ID.eq(policyId)).execute();
	}

}
