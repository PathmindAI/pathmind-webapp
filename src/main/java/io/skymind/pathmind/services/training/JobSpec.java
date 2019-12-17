package io.skymind.pathmind.services.training;

import io.skymind.pathmind.constants.RunType;

import java.util.List;
import java.util.function.Supplier;

public class JobSpec {
    private final long userId;
    private final long modelId;
    private final long experimentId;
    private final long runId;

    private final String modelFileId;

    private final String variables;
    private final String reset;
    private final String reward;
    private final String metrics = ""; // Disabled for now. Proper Metrics support will probably need a bit of
                                       // re-engineering across the webapp, Pathmind Helper and NativeRL
    private final int actions;
    private final int observations;
    private final int iterations;

    private final ExecutionEnvironment env;

    private final RunType type;

    private String parentPolicyExternalId;
    private Supplier<byte[]> snapshotSupplier;

    private final List<Double> learningRates;
    private final List<Double> gammas;
    private final List<Integer> batchSizes;

    private final int maxTimeInSec;

    public JobSpec(long userId, long modelId, long experimentId, long runId, String modelFileId, String variables, String reset, String reward, int actions, int observations, int iterations, ExecutionEnvironment env, RunType type, List<Double> learningRates, List<Double> gammas, List<Integer> batchSizes, int maxTimeInSec) {
        this.userId = userId;
        this.modelId = modelId;
        this.experimentId = experimentId;
        this.runId = runId;
        this.modelFileId = modelFileId;
        this.variables = variables;
        this.reset = reset;
        this.reward = reward;
        this.actions = actions;
        this.observations = observations;
        this.iterations = iterations;
        this.env = env;
        this.type = type;
        this.learningRates = learningRates;
        this.gammas = gammas;
        this.batchSizes = batchSizes;
        this.maxTimeInSec = maxTimeInSec;
    }

    public long getUserId() {
        return userId;
    }

    public long getModelId() {
        return modelId;
    }

    public long getExperimentId() {
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

    public byte[] getSnapshot() {
        return snapshotSupplier != null ? snapshotSupplier.get() : null;
    }

    public void setSnapshot(Supplier<byte[]> snapshotSupplier) {
        this.snapshotSupplier = snapshotSupplier;
    }

    public String getParentPolicyExternalId() {
        return parentPolicyExternalId;
    }

    public void setParentPolicyExternalId(String parentPolicyExternalId) {
        this.parentPolicyExternalId = parentPolicyExternalId;
    }

    public ExecutionEnvironment getEnv() {
        return env;
    }

    public long getRunId() {
        return runId;
    }

    public List<Double> getLearningRates() {
        return learningRates;
    }

    public List<Double> getGammas() {
        return gammas;
    }

    public List<Integer> getBatchSizes() {
        return batchSizes;
    }

    public int getMaxTimeInSec() {
        return maxTimeInSec;
    }

    public String getModelFileId() {
        return modelFileId;
    }
}
