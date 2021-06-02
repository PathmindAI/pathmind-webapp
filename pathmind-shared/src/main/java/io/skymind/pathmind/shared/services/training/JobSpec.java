package io.skymind.pathmind.shared.services.training;

import java.util.List;

import io.skymind.pathmind.shared.constants.ModelType;
import io.skymind.pathmind.shared.constants.RunType;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.shared.services.training.environment.ExecutionEnvironment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
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
    private final String metrics; // Disabled for now. Proper Metrics support will probably need a bit of
    // re-engineering across the webapp, Pathmind Helper and NativeRL
    private final List<Observation> selectedObservations;
    private final int iterations;

    private final ExecutionEnvironment env;

    private final ModelType modelType;
    private final RunType type;

    private final int maxTimeInSec;

    private final int numSamples;
    private final boolean multiAgent;
    private final boolean resume;
    private final int checkpointFrequency;
    private final boolean userLog;

    private final boolean recordMetricsRaw;
    private final boolean namedVariables;

    private final String mainAgentName;
    private final String expClassName;
    private final String expClassType;

    private final String environment;
    private final String obsSelection;
    private final String rewFctName;

    private final boolean actionMask;
}
