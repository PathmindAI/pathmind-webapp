package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.data.Observation;
import org.jooq.DSLContext;

import static io.skymind.pathmind.db.jooq.Tables.OBSERVATION;


class ObservationRepository {
    protected static long insertObservation(DSLContext ctx, Observation observation) {
        return ctx.insertInto(OBSERVATION)
                .columns(OBSERVATION.VARIABLE, OBSERVATION.DATA_TYPE, OBSERVATION.DESCRIPTION, OBSERVATION.EXAMPLE,
                        OBSERVATION.MIN, OBSERVATION.MAX, OBSERVATION.MODEL_ID)
                .values(observation.getVariable(), observation.getDataType(), observation.getDescription(), observation.getExample(),
                        observation.getMin(), observation.getMax(), observation.getModelId())
                .returning(OBSERVATION.ID)
                .fetchOne()
                .getValue(OBSERVATION.ID);
    }
}
