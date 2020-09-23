package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.data.user.DeepCloneableInterface;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardScore implements Serializable, DeepCloneableInterface {
    // May be NaN!
    private Double max;
    private Double min;
    private Double mean;

    private Integer iteration;
    private Integer episodeCount;

    @Override
    public RewardScore shallowClone() {
        return new RewardScore(max, min, mean, iteration, episodeCount);
    }
}
