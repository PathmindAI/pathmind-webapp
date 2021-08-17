package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.constants.ParamType;
import io.skymind.pathmind.shared.data.SimulationParameter;
import org.jooq.DSLContext;
import org.jooq.Query;

import java.util.List;
import java.util.stream.Collectors;

import static io.skymind.pathmind.db.jooq.Tables.SIMULATION_PARAMETER;

public class SimulationParameterRepository {
    protected static void insertOrUpdateSimulationParameter(DSLContext ctx, List<SimulationParameter> simParams) {
        simParams.forEach(p -> {
            if (p.getType() == ParamType.BOOLEAN.getValue()) {
                p.setValue(String.valueOf(Boolean.parseBoolean(p.getValue())));
            }
        });
        final List<Query> saveQueries = simParams.stream()
            .map(param ->
                ctx.insertInto(SIMULATION_PARAMETER)
                    .columns(SIMULATION_PARAMETER.MODEL_ID, SIMULATION_PARAMETER.EXPERIMENT_ID, SIMULATION_PARAMETER.INDEX,
                        SIMULATION_PARAMETER.KEY, SIMULATION_PARAMETER.VALUE, SIMULATION_PARAMETER.TYPE)
                    .values(param.getModelId(), param.getExperimentId(), param.getIndex(),
                        param.getKey(), param.getValue(), param.getType())
                    .onConflict(SIMULATION_PARAMETER.MODEL_ID, SIMULATION_PARAMETER.EXPERIMENT_ID, SIMULATION_PARAMETER.INDEX, SIMULATION_PARAMETER.KEY)
                    .doUpdate()
                    .set(SIMULATION_PARAMETER.VALUE, param.getValue()))
            .collect(Collectors.toList());

        ctx.batch(saveQueries).execute();
    }

    protected static List<SimulationParameter> getSimulationParametersForModel(DSLContext ctx, long modelId) {
        return ctx.select(SIMULATION_PARAMETER.asterisk())
            .from(SIMULATION_PARAMETER)
            .where(SIMULATION_PARAMETER.MODEL_ID.eq(modelId).and(SIMULATION_PARAMETER.EXPERIMENT_ID.isNull()))
            .orderBy(SIMULATION_PARAMETER.INDEX)
            .fetchInto(SimulationParameter.class);
    }

    protected static List<SimulationParameter> getSimulationParametersForExperiment(DSLContext ctx, long experimentId) {
        return ctx.select(SIMULATION_PARAMETER.asterisk())
            .from(SIMULATION_PARAMETER)
            .where(SIMULATION_PARAMETER.EXPERIMENT_ID.eq(experimentId))
            .orderBy(SIMULATION_PARAMETER.INDEX)
            .fetchInto(SimulationParameter.class);
    }
}
