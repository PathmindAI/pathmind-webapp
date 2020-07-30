package io.skymind.pathmind.shared.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricsRawThisEpisode  implements Serializable {
    private Integer index;
    private Double value;
}
