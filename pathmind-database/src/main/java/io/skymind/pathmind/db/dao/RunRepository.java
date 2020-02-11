package io.skymind.pathmind.db.dao;

import static io.skymind.pathmind.db.jooq.Tables.POLICY;
import static io.skymind.pathmind.db.jooq.tables.Experiment.EXPERIMENT;
import static io.skymind.pathmind.db.jooq.tables.Model.MODEL;
import static io.skymind.pathmind.db.jooq.tables.Project.PROJECT;
import static io.skymind.pathmind.db.jooq.tables.Run.RUN;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Record;

import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.constants.RunType;
import io.skymind.pathmind.db.data.Experiment;
import io.skymind.pathmind.db.data.Model;
import io.skymind.pathmind.db.data.Project;
import io.skymind.pathmind.db.data.Run;
import io.skymind.pathmind.db.jooq.Tables;
import io.skymind.pathmind.db.jooq.tables.records.RunRecord;

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

    protected static List<Run> getRuns(DSLContext ctx, List<Long> runIds) {
        return ctx
                .select(RUN.asterisk())
                .select(EXPERIMENT.asterisk())
                .select(MODEL.ID, MODEL.NAME)
                .select(PROJECT.ID, PROJECT.NAME, PROJECT.PATHMIND_USER_ID)
                .from(RUN)
                    .leftJoin(EXPERIMENT).on(EXPERIMENT.ID.eq(RUN.EXPERIMENT_ID))
                    .leftJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
                    .leftJoin(PROJECT).on(PROJECT.ID.eq(MODEL.PROJECT_ID))
                .where(RUN.ID.in(runIds))
                .fetch(record -> fetchRunLeftJoin(record));
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

    protected static List<Long> getAlreadyNotifiedOrStillExecutingRunsWithType(DSLContext ctx, long experimentId, int runType) {
    	return ctx.select(Tables.RUN.ID)
    			.from(Tables.RUN)
    			.where(Tables.RUN.EXPERIMENT_ID.eq(experimentId))
    			.and(Tables.RUN.RUN_TYPE.eq(runType))
    			.and(
    					Tables.RUN.STATUS.notIn(Arrays.asList(RunStatus.Completed.getValue(), RunStatus.Error.getValue()))
    				.or(Tables.RUN.NOTIFICATION_SENT_AT.isNotNull())
    			)
    			.fetch(Tables.RUN.ID);
    }
    
    protected static List<Long> getExecutingRuns(DSLContext ctx) {
        return ctx.selectDistinct(Tables.RUN.ID)
                .from(Tables.RUN)
                .leftOuterJoin(POLICY)
                    .on(POLICY.RUN_ID.eq(Tables.RUN.ID))
                .where(Tables.RUN.STATUS.eq(RunStatus.Starting.getValue())
                        .or(Tables.RUN.STATUS.eq(RunStatus.Running.getValue()))
                        .or(Tables.RUN.STATUS.eq(RunStatus.Completed.getValue()))
                        .or(Tables.RUN.STATUS.eq(RunStatus.Restarting.getValue()))
                        .and(POLICY.HAS_FILE.isNull().or(POLICY.HAS_FILE.isFalse())))
                .fetch(Tables.RUN.ID);
    }

    protected static Map<Long, List<String>> getStoppedPolicyNamesForRuns(DSLContext ctx, List<Long> runIds) {
        return ctx.select(POLICY.EXTERNAL_ID, POLICY.RUN_ID)
                .from(POLICY)
                .where(POLICY.RUN_ID.in(runIds)).and(POLICY.STOPPED_AT.isNotNull())
                .fetchGroups(POLICY.RUN_ID, POLICY.EXTERNAL_ID);
    }

    protected static void markAsStarting(DSLContext ctx, long runId){
        ctx.update(Tables.RUN)
                .set(Tables.RUN.STATUS, RunStatus.Starting.getValue())
                .set(Tables.RUN.STARTED_AT, LocalDateTime.now())
                .where(Tables.RUN.ID.eq(runId)).execute();
    }

    protected static void updateStatus(DSLContext ctx, Run run) {
        ctx.update(Tables.RUN)
                .set(Tables.RUN.STATUS, run.getStatus())
                .set(Tables.RUN.STOPPED_AT, run.getStoppedAt())
				.set(Tables.RUN.TRAINING_ERROR_ID, run.getTrainingErrorId())
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
}
