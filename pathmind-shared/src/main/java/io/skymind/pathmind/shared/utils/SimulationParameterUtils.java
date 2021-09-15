package io.skymind.pathmind.shared.utils;

import io.skymind.pathmind.shared.constants.ParamType;
import io.skymind.pathmind.shared.data.SimulationParameter;

import java.util.Date;
import java.util.List;

public class SimulationParameterUtils {

    public static List<SimulationParameter> makeValidSimulationParameter(Long modelId, Long experimentId, List<SimulationParameter> simulationParameters) {
        simulationParameters.forEach(p -> {
            p.setModelId(modelId);
            p.setExperimentId(experimentId);

            if (p.getType() == ParamType.DATE) {
                // Fri Feb 14 00:00:00 GMT 2020 -> "1581638400000"
                p.setValue(String.valueOf(new Date(p.getValue()).getTime()));
            }
        });
        return simulationParameters;
    }
}
