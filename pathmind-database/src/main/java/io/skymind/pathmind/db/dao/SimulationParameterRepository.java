package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.data.SimulationParameter;
import org.jooq.DSLContext;
import org.jooq.Query;

import java.util.List;
import java.util.stream.Collectors;

import static io.skymind.pathmind.db.jooq.Tables.SIMULATION_PARAMETER;

public class SimulationParameterRepository {
    protected static void insertSimulationParameter(DSLContext ctx, List<SimulationParameter> simParams) {
        final List<Query> saveQueries = simParams.stream()
            .map(param ->
                ctx.insertInto(SIMULATION_PARAMETER)
                    .columns(SIMULATION_PARAMETER.MODEL_ID, SIMULATION_PARAMETER.EXPERIMENT_ID, SIMULATION_PARAMETER.INDEX,
                        SIMULATION_PARAMETER.KEY, SIMULATION_PARAMETER.VALUE, SIMULATION_PARAMETER.TYPE)
                    .values(param.getModelId(), param.getExperimentId(), param.getIndex(),
                        param.getKey(), param.getValue(), param.getDataType()))
            .collect(Collectors.toList());

        ctx.batch(saveQueries).execute();
    }
}
