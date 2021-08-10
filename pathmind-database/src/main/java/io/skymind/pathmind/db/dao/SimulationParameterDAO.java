package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.data.SimulationParameter;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SimulationParameterDAO {
    private final DSLContext ctx;

    SimulationParameterDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void insertSimulationParameters(List<SimulationParameter> simParams) {
        SimulationParameterRepository.insertSimulationParameter(ctx, simParams);
    }
}
