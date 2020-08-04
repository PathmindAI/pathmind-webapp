package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.db.jooq.Tables;
import io.skymind.pathmind.db.jooq.tables.records.RunRecord;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.constants.RunType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.data.user.UserMetrics;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.skymind.pathmind.db.jooq.Tables.PATHMIND_USER;
import static io.skymind.pathmind.db.jooq.Tables.POLICY;
import static io.skymind.pathmind.db.jooq.tables.Experiment.EXPERIMENT;
import static io.skymind.pathmind.db.jooq.tables.Model.MODEL;
import static io.skymind.pathmind.db.jooq.tables.Project.PROJECT;
import static io.skymind.pathmind.db.jooq.tables.Run.RUN;
import static org.jooq.impl.DSL.count;

class RunRepository
{
    protected static Run getRun(DSLContext ctx, long runId) {
        return ctx
                .select(RUN.asterisk())
                .select(EXPERIMENT.asterisk())
                .select(MODEL.ID, MODEL.NAME)
                .select(PROJECT.ID, PROJECT.NAME, PROJECT.PATHMIND_USER_ID)
                .from(RUN)
                    .leftJoin(EXPERIMENT).on(EXPERIMENT.ID.eq(RUN.EXPERIMENT_ID))
                    .leftJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
                    .leftJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
                .where(RUN.ID.eq(runId))
                .fetchOne(record -> fetchRunLeftJoin(record));
    }

    private static Run fetchRunLeftJoin(Record record) {
        Run run = record.into(RUN).into(Run.class);
        run.setExperiment(record.into(EXPERIMENT).into(Experiment.class));
        run.setModel(record.into(MODEL).into(Model.class));
        run.setProject(record.into(PROJECT).into(Project.class));
        return run;
    }

    protected static Run createRun(DSLContext ctx, Experiment experiment, RunType runType){
        final RunRecord run = Tables.RUN.newRecord();
        run.setExperimentId(experiment.getId());
        run.setName(runType.toString());
        run.setRunType(runType.getValue());
        run.setStatus(RunStatus.NotStarted.getValue());
        run.attach(ctx.configuration());
        run.store();
        return run.into(new Run());
    }

    protected static Map<Long, List<Run>> getRunsForExperiments(DSLContext ctx, List<Long> experimentIds) {
        return ctx.select(Tables.RUN.asterisk())
                .from(RUN)
                .where(Tables.RUN.EXPERIMENT_ID.in(experimentIds))
                .fetchGroups(RUN.EXPERIMENT_ID, Run.class);
    }
    
    protected static List<Run> getRunsForExperiment(DSLContext ctx, Long experimentId) {
    	return ctx.select(Tables.RUN.asterisk())
    			.from(RUN)
    			.where(Tables.RUN.EXPERIMENT_ID.eq(experimentId))
    			.fetchInto(Run.class);
    }

    protected static List<Long> getAlreadyNotifiedOrStillExecutingRunsWithType(DSLContext ctx, long experimentId, int runType) {
    	return ctx.select(Tables.RUN.ID)
    			.from(Tables.RUN)
    			.where(Tables.RUN.EXPERIMENT_ID.eq(experimentId))
    			.and(Tables.RUN.RUN_TYPE.eq(runType))
    			.and(
    					Tables.RUN.STATUS.notIn(Arrays.asList(RunStatus.Completed.getValue(), RunStatus.Error.getValue(), RunStatus.Killed.getValue()))
    				.or(Tables.RUN.NOTIFICATION_SENT_AT.isNotNull())
    			)
    			.fetch(Tables.RUN.ID);
    }
    
    protected static List<Run> getExecutingRuns(DSLContext ctx) {
        return ctx.select(RUN.ID, RUN.NAME, RUN.EXPERIMENT_ID, RUN.JOB_ID, RUN.NOTIFICATION_SENT_AT, RUN.EC2_CREATED_AT, RUN.RUN_TYPE, RUN.STARTED_AT, RUN.STOPPED_AT, RUN.STATUS)
        		.select(EXPERIMENT.ID, EXPERIMENT.NAME, EXPERIMENT.MODEL_ID, EXPERIMENT.DATE_CREATED, EXPERIMENT.LAST_ACTIVITY_DATE)
        		.select(MODEL.ID, MODEL.NAME)
        		.select(PROJECT.ID, PROJECT.NAME, PROJECT.PATHMIND_USER_ID)
        		.from(RUN)
        			.leftJoin(EXPERIMENT).on(EXPERIMENT.ID.eq(RUN.EXPERIMENT_ID))
        			.leftJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
        			.leftJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
    			.where(RUN.STATUS.in(
    					RunStatus.Starting.getValue(), 
    					RunStatus.Running.getValue(),
    					RunStatus.Restarting.getValue(),
    					RunStatus.Stopping.getValue())
					.or(RUN.ID.in(
						DSL.select(POLICY.RUN_ID)
							.from(POLICY)
							.leftJoin(RUN).on(RUN.ID.eq(POLICY.RUN_ID))
							.where(
									RUN.STATUS.eq(RunStatus.Completed.getValue())
									.and(POLICY.HAS_FILE.isFalse())
                                    .and(POLICY.IS_VALID.isTrue())))))
    			.fetch(record -> fetchRunLeftJoin(record));
    }

    protected static Map<Long, List<String>> getStoppedPolicyNamesForRuns(DSLContext ctx, List<Long> runIds) {
        return ctx.select(POLICY.EXTERNAL_ID, POLICY.RUN_ID)
                .from(POLICY)
                .where(POLICY.RUN_ID.in(runIds)).and(POLICY.STOPPED_AT.isNotNull())
                .fetchGroups(POLICY.RUN_ID, POLICY.EXTERNAL_ID);
    }

    protected static void markAsStarting(DSLContext ctx, long runId, String jobId){
        ctx.update(Tables.RUN)
                .set(Tables.RUN.STATUS, RunStatus.Starting.getValue())
                .set(Tables.RUN.STARTED_AT, LocalDateTime.now())
                .set(Tables.RUN.JOB_ID, jobId)
                .where(Tables.RUN.ID.eq(runId)).execute();
    }

    protected static void updateStatus(DSLContext ctx, Run run) {
        ctx.update(Tables.RUN)
                .set(Tables.RUN.STATUS, run.getStatus())
                .set(Tables.RUN.EC2_CREATED_AT, run.getEc2CreatedAt())
                .set(Tables.RUN.STOPPED_AT, run.getStoppedAt())
				.set(Tables.RUN.TRAINING_ERROR_ID, run.getTrainingErrorId())
				.set(Tables.RUN.RLLIB_ERROR, run.getRllibError())
                .set(Tables.RUN.SUCCESS_MESSAGE, run.getSuccessMessage())
                .set(Tables.RUN.WARNING_MESSAGE, run.getWarningMessage())
                .where(Tables.RUN.ID.eq(run.getId()))
                .execute();
    }
    
    protected static void markAsNotificationSent(DSLContext ctx, long runId){
    	ctx.update(Tables.RUN)
    		.set(Tables.RUN.NOTIFICATION_SENT_AT, LocalDateTime.now())
    		.where(Tables.RUN.ID.eq(runId)).execute();
    }
    
    protected static void clearNotificationSentInfo(DSLContext ctx, long experimentId) {
		ctx.update(Tables.RUN)
			.set(Tables.RUN.NOTIFICATION_SENT_AT, (LocalDateTime) null)
			.where(Tables.RUN.EXPERIMENT_ID.eq(experimentId))
			.execute();
	}

    protected static UserMetrics getRunUsageDataForUser(DSLContext ctx, long userId) {
        Table<?> nestedToday = ctx.select(count().as("runsToday"))
                .from(RUN)
                    .leftJoin(EXPERIMENT).on(EXPERIMENT.ID.eq(RUN.EXPERIMENT_ID))
                    .leftJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
                    .leftJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
                    .leftJoin(PATHMIND_USER).on(PATHMIND_USER.ID.eq(PROJECT.PATHMIND_USER_ID))
                .where(DSL.day(RUN.STARTED_AT).eq(DSL.day(LocalDateTime.now())))
                    .and(DSL.month(RUN.STARTED_AT).eq(DSL.month(LocalDateTime.now())))
                    .and(DSL.year(RUN.STARTED_AT).eq(DSL.year(LocalDateTime.now())))
                .asTable("today");
        Table<?> nestedThisMonth = ctx.select(count().as("runsThisMonth"))
                .from(RUN)
                    .leftJoin(EXPERIMENT).on(EXPERIMENT.ID.eq(RUN.EXPERIMENT_ID))
                    .leftJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
                    .leftJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
                    .leftJoin(PATHMIND_USER).on(PATHMIND_USER.ID.eq(PROJECT.PATHMIND_USER_ID))
                .where(DSL.month(RUN.STARTED_AT).eq(DSL.month(LocalDateTime.now())))
                    .and(DSL.year(RUN.STARTED_AT).eq(DSL.year(LocalDateTime.now())))
                .asTable("thisMonday");
        Record record = ctx.select(nestedToday.field("runsToday"), nestedThisMonth.field("runsThisMonth"))
                .from(nestedToday, nestedThisMonth)
                .fetchOne();

        // HOTFIX - TODO - remove this after debugging
        // Users are being prevented from running experiments
        return new UserMetrics(0, 0);

        // Must be a customer with no experiments.
        if(record == null) {
            return new UserMetrics(0, 0);
        }

        return new UserMetrics(
                (Integer)record.getValue("runsToday"),
                (Integer)record.getValue("runsThisMonth"));
    }
}
