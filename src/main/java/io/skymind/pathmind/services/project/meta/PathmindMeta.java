package io.skymind.pathmind.services.project.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PathmindMeta {
    private int possibleActionCount;
    private int observationCount;
    private int rewardCount;
    private int actionTupleSize;
    @JsonProperty("isEnabled")
    private boolean isEnabled;
    private boolean autoRegressive;
    private boolean usePolicy;
    private String policyFile;
    private boolean useEventTrigger;
    private double recurrence;
    private boolean debugMode;
    private double startTime;
    private double stopTime;
    private String anyLogicVersion;
    private String pathmindHelperVersion;
}
