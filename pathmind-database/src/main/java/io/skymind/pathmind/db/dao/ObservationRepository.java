package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.data.Observation;
import org.jooq.DSLContext;
import org.jooq.Query;

import static io.skymind.pathmind.db.jooq.Tables.OBSERVATION;

import java.util.List;
import java.util.stream.Collectors;


class ObservationRepository {
    
    protected static void insertOrUpdateObservations(DSLContext ctx, List<Observation> observations) {
        final List<Query> saveQueries = observations.stream()
                .map(observation ->
                        ctx.insertInto(OBSERVATION)
                            .columns(OBSERVATION.MODEL_ID, OBSERVATION.ARRAY_INDEX, OBSERVATION.VARIABLE, OBSERVATION.DATA_TYPE, OBSERVATION.DESCRIPTION,
                                    OBSERVATION.EXAMPLE, OBSERVATION.MIN, OBSERVATION.MAX, OBSERVATION.MIN_ITEMS, OBSERVATION.MAX_ITEMS)
                            .values(observation.getModelId(), observation.getArrayIndex(), observation.getVariable(), observation.getDataType(), observation.getDescription(),
                                    observation.getExample(), observation.getMin(), observation.getMax(), observation.getMinItems(), observation.getMaxItems())
                            .onConflict(OBSERVATION.MODEL_ID, OBSERVATION.ARRAY_INDEX)
                                .doUpdate()
                                .set(OBSERVATION.VARIABLE, observation.getVariable())
                                .set(OBSERVATION.DATA_TYPE, observation.getDataType())
                                .set(OBSERVATION.DESCRIPTION, observation.getDescription())
                                .set(OBSERVATION.EXAMPLE, observation.getExample())
                                .set(OBSERVATION.MIN, observation.getMin())
                                .set(OBSERVATION.MAX, observation.getMax())
                                .set(OBSERVATION.MIN_ITEMS, observation.getMinItems())
                                .set(OBSERVATION.MAX_ITEMS, observation.getMaxItems()))
                .collect(Collectors.toList());

        ctx.batch(saveQueries).execute();
    }
    
    protected static List<Observation> getObservationsForModel(DSLContext ctx, long modelId) {
        return ctx.select(OBSERVATION.asterisk())
                .from(OBSERVATION)
                .where(OBSERVATION.MODEL_ID.eq(modelId))
                .fetchInto(Observation.class);
    }
}
