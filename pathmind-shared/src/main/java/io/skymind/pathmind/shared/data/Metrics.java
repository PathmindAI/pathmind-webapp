package io.skymind.pathmind.shared.data;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Metrics implements Serializable {
    private Integer agent;
    private Integer iteration;
    private Integer index;
    private Double max;
    private Double min;
    private Double mean;
}
