package io.skymind.pathmind.db.dao;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import io.skymind.pathmind.shared.data.Observation;

@Repository
public class ObservationDAO {
    private final DSLContext ctx;

    ObservationDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void deleteModelObservations(long modelId) {
        ObservationRepository.deleteModelObservations(ctx, modelId);
    }

    public List<Observation> getObservationsForModel(long modelId) {
        return ObservationRepository.getObservationsForModel(ctx, modelId);
    }

    public void saveObservations(List<Observation> observations) {
        ObservationRepository.insertOrUpdateObservations(ctx, observations);
    }
}