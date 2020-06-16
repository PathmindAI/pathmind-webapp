package io.skymind.pathmind.db.dao;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import io.skymind.pathmind.shared.data.Observation;

@Repository
public class ObservationDAO {
    private final DSLContext ctx;

    ObservationDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public List<Observation> getObservationsForModel(long modelId) {
        return ObservationRepository.getObservationsForModel(ctx, modelId);
    }

    public void updateModelObservations(long modelId, List<Observation> observations) {
        ctx.transaction(conf -> {
            DSLContext transactionCtx = DSL.using(conf);
            ObservationRepository.deleteModelObservations(transactionCtx, modelId);
            if (observations != null) {
                observations.forEach(obs -> obs.setModelId(modelId));
                ObservationRepository.insertOrUpdateObservations(transactionCtx, observations);
            }
        });
    }
}