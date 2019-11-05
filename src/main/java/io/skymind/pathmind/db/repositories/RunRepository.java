package io.skymind.pathmind.db.repositories;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.Run;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import static io.skymind.pathmind.data.db.tables.Experiment.EXPERIMENT;
import static io.skymind.pathmind.data.db.tables.Model.MODEL;
import static io.skymind.pathmind.data.db.tables.Project.PROJECT;
import static io.skymind.pathmind.data.db.tables.Run.RUN;

@Repository
public class RunRepository
{
    public static Run getRun(DSLContext ctx, long runId) {
        Record record = ctx
                .select(RUN.asterisk())
                .select(EXPERIMENT.asterisk())
                .select(MODEL.ID, MODEL.NAME, MODEL.GET_OBSERVATION_FOR_REWARD_FUNCTION)
                .select(PROJECT.ID, PROJECT.NAME, PROJECT.PATHMIND_USER_ID)
                .from(RUN)
                    .leftJoin(EXPERIMENT)
                .on(EXPERIMENT.ID.eq(RUN.EXPERIMENT_ID))
                    .leftJoin(MODEL)
                .on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
                    .leftJoin(PROJECT)
                .on(PROJECT.ID.eq(MODEL.PROJECT_ID))
                .where(RUN.ID.eq(runId))
                .fetchOne();

        Run run = record.into(RUN).into(Run.class);
        run.setExperiment(record.into(EXPERIMENT).into(Experiment.class));
        run.setModel(record.into(MODEL).into(Model.class));
        run.setProject(record.into(PROJECT).into(Project.class));
        return run;
    }
}
