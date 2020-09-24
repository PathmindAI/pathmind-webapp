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
public class Metrics implements Serializable, DeepCloneableInterface {
    private Integer iteration;
    private List<MetricsThisIter> metricsThisIter;

    @Override
    public Metrics shallowClone() {
        return new Metrics(iteration, CloneUtils.shallowCloneList(metricsThisIter));
    }
}
