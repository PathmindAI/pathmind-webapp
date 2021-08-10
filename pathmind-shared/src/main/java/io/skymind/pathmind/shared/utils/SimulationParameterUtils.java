package io.skymind.pathmind.shared.utils;

import io.skymind.pathmind.shared.constants.ParamType;
import io.skymind.pathmind.shared.data.SimulationParameter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SimulationParameterUtils {
    public static List<SimulationParameter> convertToSimulationParams(Long modelId, Long experimentId, Map<String, Object> paramMap) {
        AtomicInteger index = new AtomicInteger();
        return paramMap.entrySet().stream()
            .map(param -> {
                String key = param.getKey();
                Object value = param.getValue();
                return new SimulationParameter(modelId, experimentId, index.getAndIncrement(),
                    ParamType.getEnumFromClass(value.getClass()).getValue(), key, value.toString());
            })
            .collect(Collectors.toList());
    }
}
