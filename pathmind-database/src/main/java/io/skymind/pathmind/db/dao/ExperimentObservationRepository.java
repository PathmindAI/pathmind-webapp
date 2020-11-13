package io.skymind.pathmind.db.dao;

import org.jooq.DSLContext;

import static io.skymind.pathmind.db.jooq.Tables.EXPERIMENT_OBSERVATION;


class ExperimentObservationRepository {
    private ExperimentObservationRepository() {
    }

    static void deleteExperimentObservations(DSLContext ctx, long experimentId) {
        ctx.deleteFrom(EXPERIMENT_OBSERVATION).where(EXPERIMENT_OBSERVATION.EXPERIMENT_ID.eq(experimentId)).execute();
    }

}
