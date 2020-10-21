package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.data.user.DeepCloneableInterface;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricsRaw implements Serializable, DeepCloneableInterface<MetricsRaw> {
    private Integer agent;
    private Integer iteration;
    private Integer episode;
    private Integer index;
    private Double value;

    @Override
    public MetricsRaw shallowClone() {
        return new MetricsRaw(agent,iteration, episode, index, value);
    }
}
