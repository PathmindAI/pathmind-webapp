package io.skymind.pathmind.db.dao;

import static io.skymind.pathmind.data.db.Tables.*;
import static io.skymind.pathmind.data.db.tables.Experiment.EXPERIMENT;
import static io.skymind.pathmind.data.db.tables.Model.MODEL;
import static io.skymind.pathmind.data.db.tables.Project.PROJECT;
import static io.skymind.pathmind.data.db.tables.Run.RUN;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Record;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.db.Tables;
import io.skymind.pathmind.data.db.tables.records.RunRecord;

class RunRepository
{
    protected static Run getRun(DSLContext ctx, long runId) {
        return ctx
                .select(RUN.asterisk())
                .select(EXPERIMENT.asterisk())
                .select(MODEL.ID, MODEL.NAME, MODEL.GET_OBSERVATION_FOR_REWARD_FUNCTION)
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
                .select(MODEL.ID, MODEL.NAME, MODEL.GET_OBSERVATION_FOR_REWARD_FUNCTION)
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

    protected static List<Run> getRunsForExperiment(DSLContext ctx, long experimentId) {
        return ctx.select(Tables.RUN.asterisk())
                .from(Tables.RUN)
                .where(Tables.RUN.EXPERIMENT_ID.eq(experimentId))
                .fetchInto(Run.class);
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
                .leftOuterJoin(POLICY_FILE)
                    .on(POLICY.ID.eq(POLICY_FILE.POLICY_ID))
                .where(Tables.RUN.STATUS.eq(RunStatus.Starting.getValue())
                        .or(Tables.RUN.STATUS.eq(RunStatus.Running.getValue()))
                        .or(Tables.RUN.STATUS.eq(RunStatus.Completed.getValue()))
                        .and(POLICY_FILE.FILE.isNull()))
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
                .where(Tables.RUN.ID.eq(run.getId()))
                .execute();
    }
    
    protected static void markAsNotificationSent(DSLContext ctx, long runId){
    	ctx.update(Tables.RUN)
    	.set(Tables.RUN.NOTIFICATION_SENT_AT, LocalDateTime.now())
    	.where(Tables.RUN.ID.eq(runId)).execute();
    }

    protected static int getRunType(DSLContext ctx, long runId) {
        return ctx.select(Tables.RUN.RUN_TYPE)
                .from(Tables.RUN)
                .where(Tables.RUN.ID.eq(runId))
                .fetchOneInto(Integer.class).intValue();
    }
}
