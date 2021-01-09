package io.skymind.pathmind.shared.data;

import java.io.Serializable;

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

    public Metrics(Integer agent, Integer iteration, Integer index, Double mean) {
        this.agent = agent;
        this.iteration = iteration;
        this.index = index;
        this.mean = mean;
    }

    @Override
    public Metrics shallowClone() {
        return new Metrics(agent, iteration, index, max, min, mean);
    }
}
