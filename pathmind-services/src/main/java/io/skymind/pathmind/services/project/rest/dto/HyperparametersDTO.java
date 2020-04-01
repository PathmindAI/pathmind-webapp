package io.skymind.pathmind.services.project.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HyperparametersDTO {

    @NotBlank(message = "Number of actions cannot be blank")
    private String actions;

    @NotBlank(message = "Number of observations cannot be blank")
    private String observations;

    @NotBlank(message = "Reward variables count cannot be blank")
    private String rewardVariablesCount;

    @NotBlank(message = "rewardFunction cannot be blank")
    private String rewardFunction;

    public static HyperparametersDTO of(@NotEmpty List<String> hyperparametersList){
       return new HyperparametersDTO(hyperparametersList.get(0), hyperparametersList.get(1), hyperparametersList.get(2), hyperparametersList.get(3));
    }
}
