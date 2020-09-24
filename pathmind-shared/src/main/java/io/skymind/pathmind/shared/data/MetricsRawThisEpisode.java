package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.data.user.DeepCloneableInterface;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricsRawThisEpisode  implements Serializable, DeepCloneableInterface {
    private Integer index;
    private Double value;

    @Override
    public MetricsRawThisEpisode shallowClone() {
        return new MetricsRawThisEpisode(index, value);
    }
}
