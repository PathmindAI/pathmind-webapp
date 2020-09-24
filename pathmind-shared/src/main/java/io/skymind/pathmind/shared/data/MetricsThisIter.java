package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.data.user.DeepCloneableInterface;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricsThisIter implements Serializable, DeepCloneableInterface {
    private Integer index;
    private Double max;
    private Double min;
    private Double mean;

    @Override
    public MetricsThisIter shallowClone() {
        return new MetricsThisIter(index, max, min, mean);
    }
}
