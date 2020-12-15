package io.skymind.pathmind.shared.data;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricsRaw implements Serializable {
    private Integer agent;
    private Integer iteration;
    private Integer episode;
    private Integer index;
    private Double value;
}
