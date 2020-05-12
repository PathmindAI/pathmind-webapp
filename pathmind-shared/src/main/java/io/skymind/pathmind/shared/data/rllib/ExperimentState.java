package io.skymind.pathmind.shared.data.rllib;

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


