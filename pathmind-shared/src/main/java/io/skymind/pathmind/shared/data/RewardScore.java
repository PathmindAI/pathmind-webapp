package io.skymind.pathmind.shared.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RewardScore {
    // May be NaN!
    private Double max;
    private Double min;
    private Double mean;

    private Integer iteration;
    private Integer episodeCount;

    public RewardScore(Double max, Double min, Double mean, Integer iteration, Integer episodeCount) {
        this.max = max;
        this.min = min;
        this.mean = mean;
        this.iteration = iteration;
        this.episodeCount = episodeCount;
    }

}
