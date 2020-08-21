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
    private boolean oldVersionFound;

    @NotBlank(message = "Number of observations cannot be blank")
    private String observations;

    @NotBlank(message = "Reward variables cannot be empty")
    private List<String> rewardVariables;

    @NotBlank(message = "rewardFunction cannot be blank")
    private String rewardFunction;
}
