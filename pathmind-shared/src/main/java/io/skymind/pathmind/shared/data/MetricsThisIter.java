package io.skymind.pathmind.shared.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricsThisIter implements Serializable {
    private Integer index;
    private Double max;
    private Double min;
    private Double mean;
    private Integer iteration;

    public MetricsThisIter(Integer index, Double max, Double min, Double mean) {
        this(index, max, min, mean, null);
    }

}
