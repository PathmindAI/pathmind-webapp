package io.skymind.pathmind.services.training;

import java.io.InputStream;
import java.util.function.Supplier;

public class JobSpec {
    private final int userId;
    private final int modelId;
    private final int experimentId;

    private final String variables;
    private final String reset;
    private final String reward;
    private final String metrics = ""; // TODO
    private final int actions;
    private final int observations;
    private final int iterations;

    private final ExecutionEnvironment env;

    private final RunType type;
    private final Supplier<InputStream> modelFileSupplier;

    public JobSpec(int userId, int modelId, int experimentId, String variables, String reset, String reward, int actions, int observations, int iterations, ExecutionEnvironment env, RunType type, Supplier<InputStream> modelFileSupplier) {
        this.userId = userId;
        this.modelId = modelId;
        this.experimentId = experimentId;
        this.variables = variables;
        this.reset = reset;
        this.reward = reward;
        this.actions = actions;
        this.observations = observations;
        this.iterations = iterations;
        this.env = env;
        this.type = type;
        this.modelFileSupplier = modelFileSupplier;
    }

    public int getUserId() {
        return userId;
    }

    public int getModelId() {
        return modelId;
    }

    public int getExperimentId() {
        return experimentId;
    }

    public String getVariables() {
        return variables;
    }

    public String getReset() {
        return reset;
    }

    public String getReward() {
        return reward;
    }

    public String getMetrics() {
        return metrics;
    }

    public int getActions() {
        return actions;
    }

    public int getObservations() {
        return observations;
    }

    public int getIterations() {
        return iterations;
    }

    public RunType getType() {
        return type;
    }

    public InputStream getModelInputStream() {
        return modelFileSupplier.get();
    }

    public ExecutionEnvironment getEnv() {
        return env;
    }
}
