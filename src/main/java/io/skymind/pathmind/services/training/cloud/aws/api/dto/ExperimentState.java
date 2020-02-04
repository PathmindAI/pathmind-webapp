package io.skymind.pathmind.services.training.cloud.aws.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExperimentState {
    private List<CheckPoint> checkpoints;
}


