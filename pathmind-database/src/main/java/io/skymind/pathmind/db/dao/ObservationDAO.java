package io.skymind.pathmind.db.dao;

import java.util.Collection;
import java.util.List;

import io.skymind.pathmind.shared.data.Observation;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
public class ObservationDAO {
    private final DSLContext ctx;

    ObservationDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public List<Observation> getObservationsForModel(long modelId) {
        return ObservationRepository.getObservationsForModel(ctx, modelId);
    }

    public List<Observation> getObservationsForExperiment(long experimentId) {
        return ObservationRepository.getObservationsForExperiment(ctx, experimentId);
    }

    public void updateModelObservations(long modelId, List<Observation> observations) {
        observations.forEach(obs -> obs.setModelId(modelId));
        ctx.transaction(conf -> {
            DSLContext transactionCtx = DSL.using(conf);
            ObservationRepository.insertOrUpdateObservations(transactionCtx, observations);
        });
    }

}