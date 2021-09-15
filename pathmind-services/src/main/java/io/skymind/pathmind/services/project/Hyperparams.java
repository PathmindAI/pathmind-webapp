package io.skymind.pathmind.services.project;

import io.skymind.pathmind.shared.data.SimulationParameter;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class Hyperparams {

    private final int numAction;

    private final int numObservation;

    private final String rewardVariableFunction;

    private final String modelType;

    private final int numberOfAgents;

    private final boolean actionMask;

    @Builder.Default
    private final List<String> rewardVariableNames = new ArrayList<>();

    @Builder.Default
    private final List<String> rewardVariableTypes = new ArrayList<>();

    @Builder.Default
    private final List<String> observationNames = new ArrayList<>();

    @Builder.Default
    private final List<String> observationTypes = new ArrayList<>();

    @Builder.Default
    private final List<SimulationParameter> simulationParams = new ArrayList<>();

}
