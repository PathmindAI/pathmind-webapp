package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.db.Tables;
import io.skymind.pathmind.data.db.tables.records.RunRecord;
import io.skymind.pathmind.db.repositories.RunRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static io.skymind.pathmind.data.db.Tables.RUN;

@Repository
public class RunDAO extends RunRepository
{

    private final DSLContext ctx;

    public RunDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public Run createRun(Experiment experiment, RunType runType){
        final RunRecord run = Tables.RUN.newRecord();
        run.setExperimentId(experiment.getId());
        run.setName(runType.toString());
        run.setRunType(runType.getValue());
        run.setStatus(RunStatus.NotStarted.getValue());
        run.attach(ctx.configuration());
        run.store();

        return run.into(new Run());
    }

    public void markAsStarting(long runId){
        ctx.update(Tables.RUN).
                set(Tables.RUN.STATUS, RunStatus.Starting.getValue())
                .set(Tables.RUN.STARTED_AT, LocalDateTime.now())
                .where(Tables.RUN.ID.eq(runId)).execute();
    }



    public List<Run> getRunsForExperiment(long experimentId) {
        return ctx
                .select(RUN.asterisk())
                .from(RUN)
                .where(RUN.EXPERIMENT_ID.eq(experimentId))
                .fetchInto(Run.class);
    }

    public Run getRun(long runId) {
        return ctx.select(RUN.asterisk())
                .from(RUN)
                .where(RUN.ID.eq(runId))
                .fetchOneInto(Run.class);
    }
}
