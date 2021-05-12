package io.skymind.pathmind.services.project;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Hyperparams {

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

}
