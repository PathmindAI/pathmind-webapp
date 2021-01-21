package io.skymind.pathmind.shared.data;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardScore implements Serializable, DeepCloneableInterface<RewardScore> {
    // May be NaN!
    private Double max;
    private Double min;
    private Double mean;

    private Integer iteration;
    private Integer episodeCount;

    public RewardScore(Double mean, Integer iteration, Integer episodeCount) {
        this.mean = mean;
        this.iteration = iteration;
        this.episodeCount = episodeCount;
    }

    @Override
    public RewardScore shallowClone() {
        return new RewardScore(max, min, mean, iteration, episodeCount);
    }
}
