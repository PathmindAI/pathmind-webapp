package io.skymind.pathmind.services.training.cloud.aws.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEven {
    @JsonProperty("run")
    Long run;
    @JsonProperty("policies")
    List<Long> policies;
}

