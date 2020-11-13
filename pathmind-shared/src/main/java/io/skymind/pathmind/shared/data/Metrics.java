package io.skymind.pathmind.shared.data;

import java.io.Serializable;

import io.skymind.pathmind.shared.data.user.DeepCloneableInterface;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Metrics implements Serializable, DeepCloneableInterface<Metrics> {
    private Integer agent;
    private Integer iteration;
    private Integer index;
    private Double max;
    private Double min;
    private Double mean;

    @Override
    public Metrics shallowClone() {
        return new Metrics(agent, iteration, index, max, min, mean);
    }
}
