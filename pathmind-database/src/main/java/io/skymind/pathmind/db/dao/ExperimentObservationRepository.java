package io.skymind.pathmind.db.dao;

import org.jooq.DSLContext;

import static io.skymind.pathmind.db.jooq.Tables.EXPERIMENT_OBSERVATION;


// TODO -> STEPH -> It's not clear which is why with the Observation and experiment observation table because there is code in the observationRepository that seems to only
// do SELECT on EXPERIMENT_OBSERVATION. This definitely needs to be cleaned up.
class ExperimentObservationRepository {
    private ExperimentObservationRepository() {
    }

    static void deleteExperimentObservations(DSLContext ctx, long experimentId) {
        ctx.deleteFrom(EXPERIMENT_OBSERVATION).where(EXPERIMENT_OBSERVATION.EXPERIMENT_ID.eq(experimentId)).execute();
    }

}
