package io.skymind.pathmind.db.dao;

import static io.skymind.pathmind.db.jooq.Tables.EXPERIMENT_OBSERVATION;

import org.jooq.DSLContext;


class ExperimentObservationRepository {
    private ExperimentObservationRepository() {
    }

    static void deleteExperimentObservations(DSLContext ctx, long experimentId) {
        ctx.deleteFrom(EXPERIMENT_OBSERVATION).where(EXPERIMENT_OBSERVATION.EXPERIMENT_ID.eq(experimentId));
    }

}
