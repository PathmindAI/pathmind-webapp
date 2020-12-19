package io.skymind.pathmind.db.dao;

import org.jooq.DSLContext;

import static io.skymind.pathmind.db.jooq.Tables.EXPERIMENT_OBSERVATION;


// REFACTOR -> https://github.com/SkymindIO/pathmind-webapp/issues/2596 For example the SQL insert is in ObservationRepository but the
// delete is in here in ExperimentObservationRepository. Right now it's not too bad because we have minimal methods but it's confusing
// which code should go where, and over time this will increase in confusion depending on who implements what.
class ExperimentObservationRepository {
    private ExperimentObservationRepository() {
    }

    static void deleteExperimentObservations(DSLContext ctx, long experimentId) {
        ctx.deleteFrom(EXPERIMENT_OBSERVATION).where(EXPERIMENT_OBSERVATION.EXPERIMENT_ID.eq(experimentId)).execute();
    }

}
