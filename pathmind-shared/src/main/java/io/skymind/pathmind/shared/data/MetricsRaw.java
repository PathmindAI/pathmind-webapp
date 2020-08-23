package io.skymind.pathmind.shared.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricsRaw implements Serializable {
    private Integer iteration;

    // episode, raw data
    private List<List<MetricsRawThisEpisode>> episodeRaw;
}
