package io.skymind.pathmind.shared.services.training;

import java.util.List;

import io.skymind.pathmind.shared.constants.RunType;
import io.skymind.pathmind.shared.services.training.environment.ExecutionEnvironment;
import lombok.Getter;
import lombok.Setter;

@Getter
public class JobSpec {
    private final long userId;
    private final long modelId;
    private final long experimentId;
    private final long runId;

    private final String modelFileId;
    @Setter
    private String checkpointFileId;

    private final String variables;
    private final String reset;
    private final String reward;
    private final String metrics = ""; // Disabled for now. Proper Metrics support will probably need a bit of
                                       // re-engineering across the webapp, Pathmind Helper and NativeRL
    private final List<String> selectedObservations;
    private final int iterations;

    private final ExecutionEnvironment env;

    private final RunType type;

    private final int maxTimeInSec;

    private final int numSamples;
    private final boolean multiAgent;
    private final boolean resume;
    private final int checkpointFrequency;
    private final boolean userLog;

    private final boolean recordMetricsRaw;
    private final boolean namedVariables;
    public JobSpec(long userId, long modelId, long experimentId, long runId, String modelFileId, String variables, String reset, String reward, List<String> selectedObservations, int iterations, ExecutionEnvironment env, RunType type, int maxTimeInSec, int numSamples, boolean multiAgent, boolean resume, int checkpointFrequency, boolean userLog, boolean recordMetricsRaw, boolean namedVariables) {
        this.userId = userId;
        this.modelId = modelId;
        this.experimentId = experimentId;
        this.runId = runId;
        this.modelFileId = modelFileId;
        this.variables = variables;
        this.reset = reset;
        this.reward = reward;
        this.selectedObservations = selectedObservations;
        this.iterations = iterations;
        this.env = env;
        this.type = type;
        this.maxTimeInSec = maxTimeInSec;
        this.numSamples = numSamples;
        this.multiAgent = multiAgent;
        this.resume = resume;
        this.checkpointFrequency = checkpointFrequency;
        this.userLog = userLog;
        this.recordMetricsRaw = recordMetricsRaw;
        this.namedVariables = namedVariables;
    }
}
