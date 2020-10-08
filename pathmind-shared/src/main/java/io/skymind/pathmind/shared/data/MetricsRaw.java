package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.data.user.DeepCloneableInterface;
import io.skymind.pathmind.shared.utils.CloneUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricsRaw implements Serializable, DeepCloneableInterface<MetricsRaw> {
    private Integer iteration;

    // episode, raw data
    private List<List<MetricsRawThisEpisode>> episodeRaw;

    @Override
    public MetricsRaw shallowClone() {
        return new MetricsRaw(iteration, getShallowCloneOfEpisodeRaw());
    }

    private List<List<MetricsRawThisEpisode>> getShallowCloneOfEpisodeRaw() {
        ArrayList<List<MetricsRawThisEpisode>> clone = new ArrayList<>(episodeRaw.size());
        episodeRaw.stream().forEach(episodeRawList ->
            clone.add(CloneUtils.shallowCloneList(episodeRawList)));
        return clone;
    }
}
