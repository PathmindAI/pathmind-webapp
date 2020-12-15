package io.skymind.pathmind.shared.data;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardScore implements Serializable {
    // May be NaN!
    private Double max;
    private Double min;
    private Double mean;

    private Integer iteration;
    private Integer episodeCount;
}
