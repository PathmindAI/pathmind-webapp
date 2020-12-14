package io.skymind.pathmind.services.project;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class AnyLogicModelInfo {
    // super class of anylogic agent
    private static final String AGENT_SUPER_CLASS = "com.anylogic.engine.Agent";

    // super class of supported experiment
    private static final String SIMULATION_SUPER_CLASS = "com.anylogic.engine.ExperimentSimulation";
    private static final String RLEXPERIMENT_SUPER_CLASS = "com.anylogic.engine.ExperimentReinforcementLearning";

    private static final String PM_HELPER_CLASS = "pathmind.policyhelper.PathmindHelper";

    private static final String PM_OBSERVATIONS = "Observations";
    private static final String RL_OBSERVATION = "Observation";

    private static final String PM_ACTIONS = "Actions";
    private static final String RL_ACTION = "Action";

    private static final String PM_REWARD = "Reward";
    private static final String RL_CONFIG = "Configuration";

    private static List<String> supportedExperiment = List.of(SIMULATION_SUPER_CLASS, RLEXPERIMENT_SUPER_CLASS);
    private static List<String> observations = List.of(PM_OBSERVATIONS, RL_OBSERVATION);
    private static List<String> actions = List.of(PM_ACTIONS, RL_ACTION);
    private static List<String> reward = List.of(PM_REWARD);
    private static List<String> config = List.of(RL_CONFIG);

    enum ExperimentType {
        Simulation,
        RLExperiment;

        public static ExperimentType getExperiment(String superClass) {
            superClass = superClass.replaceAll("/", ".");
            if (superClass.equals(SIMULATION_SUPER_CLASS)) {
                return Simulation;
            } else if (superClass.equals(RLEXPERIMENT_SUPER_CLASS)){
                return RLExperiment;
            } else {
                throw new IllegalStateException(superClass + " is not supported class for Experiment");
            }
        }
    }

    public static boolean isSupportedExperiment(String superClass) {
        return supportedExperiment.contains(superClass.replaceAll("/", "."));
    }

    public static boolean isObservations(String className) {
        return observations.contains(className);
    }

    public static boolean isActions(String className) {
        return actions.contains(className);
    }

    public static boolean isReward(String className) {
        return reward.contains(className);
    }

    public static boolean isConfig(String className) {
        return config.contains(className);
    }

    public static boolean isHelperClass(String className) {
        return PM_HELPER_CLASS.equals(className);
    }

    public static String getNameFromClass(String className) {
        String[] split = className.replaceAll("/", ".").split("\\.");
        return split.length > 1 ? split[split.length -1] : "";
    }


    private String experimentClass;
    private String superClass;
    private String mainAgentClass;
    private ExperimentType experimentType;
//    private String observationClass;
//    private String actionClass;
//    private String rewardClass;
//    private String configurationClass;

    public AnyLogicModelInfo(String experimentClass, String superClass) {
        this.experimentClass = experimentClass;
        this.superClass = superClass;
        this.experimentType = ExperimentType.getExperiment(superClass);
    }
}
