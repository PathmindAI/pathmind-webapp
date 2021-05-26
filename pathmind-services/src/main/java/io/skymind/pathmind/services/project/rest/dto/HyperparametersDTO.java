package io.skymind.pathmind.services.project.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HyperparametersDTO {
    @NotBlank(message = "isEnabled cannot be blank")
    private boolean isEnabled;

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
