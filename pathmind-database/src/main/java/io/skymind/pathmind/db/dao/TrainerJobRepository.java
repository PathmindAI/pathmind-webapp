package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.TrainerJob;
import org.jooq.DSLContext;

import static io.skymind.pathmind.db.jooq.tables.TrainerJob.TRAINER_JOB;

public class TrainerJobRepository {
    protected static TrainerJob getTrainerJob(DSLContext ctx, String trainerJobId) {
        return ctx
                .select(TRAINER_JOB.asterisk())
                .from(TRAINER_JOB)
                .where(TRAINER_JOB.JOB_ID.eq(trainerJobId))
                .fetchOneInto(TrainerJob.class);
    }

    protected static RunStatus getStatus(DSLContext ctx, String trainerJobId) {
        return ctx
                .select(TRAINER_JOB.STATUS)
                .from(TRAINER_JOB)
                .where(TRAINER_JOB.JOB_ID.eq(trainerJobId))
                .fetchOneInto(RunStatus.class);
    }
}
