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

    public List<Observation> getObservationsForExperiment(long experimentId) {
        return ObservationRepository.getObservationsForModel(ctx, experimentId);
    }

    public void updateModelObservations(long modelId, List<Observation> observations) {
        observations.forEach(obs -> obs.setModelId(modelId));
        ctx.transaction(conf -> {
            DSLContext transactionCtx = DSL.using(conf);
            ObservationRepository.insertOrUpdateObservations(transactionCtx, observations);
        });
    }
    
    public void saveExperimentObservations(long experimentId, List<Observation> observations) {
        ctx.transaction(conf -> {
            DSLContext transactionCtx = DSL.using(conf);
            ExperimentObservationRepository.deleteExperimentObservations(transactionCtx, experimentId);
            ObservationRepository.insertExperimentObservations(transactionCtx, experimentId, observations);
        });
    }
}