package io.skymind.pathmind.services.project.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HyperparametersDTO {
    @NotBlank(message = "Number of actions cannot be blank")
    private String actions;

    @NotBlank(message = "isEnabled cannot be blank")
    private boolean isEnabled;

    //todo after 1 release, we need to uncomment it. it's for supporting previous MA.
    //@NotBlank(message = "agentParams cannot be blank")
    private Map<String, Object> agentParams;

    @NotBlank(message = "oldVersionFound cannot be blank")
    private boolean oldVersionFound;

    @NotBlank(message = "Number of observations cannot be blank")
    private String observations;
    
    @NotBlank(message = "Observation names cannot be empty")
    private List<String> observationNames;

    @NotBlank(message = "Observation types cannot be empty")
    private List<String> observationTypes;

    @NotBlank(message = "Reward variable names cannot be empty")
    private List<String> rewardVariableNames;

    @NotBlank(message = "Reward variable types cannot be empty")
    private List<String> rewardVariableTypes;

    @NotBlank(message = "rewardFunction cannot be blank")
    private String rewardFunction;

    @NotBlank(message = "Number of agents cannot be blank")
    private String agents;

    @NotBlank(message = "mode(modelType) cannot be blank")
    private String mode;

    @NotBlank(message = "Action mask cannot be blank")
    private boolean isActionMask;

    private String failedSteps;
}
