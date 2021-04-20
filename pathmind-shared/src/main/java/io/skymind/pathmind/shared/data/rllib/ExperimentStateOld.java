package io.skymind.pathmind.shared.data.rllib;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExperimentStateOld {
    private List<CheckPoint> checkpoints;
}


