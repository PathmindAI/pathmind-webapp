package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.data.user.DeepCloneableInterface;
import io.skymind.pathmind.shared.utils.CloneUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

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
