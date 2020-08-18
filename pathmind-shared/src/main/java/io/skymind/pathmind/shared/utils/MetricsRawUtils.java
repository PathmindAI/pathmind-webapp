package io.skymind.pathmind.shared.utils;

import io.skymind.pathmind.shared.data.MetricsRaw;
import io.skymind.pathmind.shared.data.MetricsRawThisEpisode;

import java.util.*;
import java.util.stream.Collectors;

public class MetricsRawUtils {
    private MetricsRawUtils()  {
        throw new IllegalAccessError("Static usage only");
    }

    public static Map<Integer, List<Double>> toIndexAndMetricRawData(List<MetricsRaw> metricsRaws) {
        // index, metrics raw data list
        Map<Integer, List<Double>> uncertaintyMap = new HashMap<>();
        if (metricsRaws != null && metricsRaws.size() > 0) {
            metricsRaws.stream()
                .forEach(metricsRaw -> {
                        List<List<MetricsRawThisEpisode>> episodeRawData = metricsRaw.getEpisodeRaw();
                        for (int episode = 0; episode < episodeRawData.size(); episode++) {
                            List<MetricsRawThisEpisode> indexRaw = episodeRawData.get(episode);
                            for (int idx = 0; idx < indexRaw.size(); idx++) {
                                List<Double> data = uncertaintyMap.containsKey(idx) ? uncertaintyMap.get(idx) : new ArrayList<>();
                                data.add(indexRaw.get(idx).getValue());
                                uncertaintyMap.put(idx, data);
                            }

                        }
                    }
                );
        }

        return uncertaintyMap;
    }

    public static List<List<MetricsRawThisEpisode>> toMetricsRawDataList(String rawDataString, int episodesThisIter, int numReward) {
        // since `hist_stats/metrics_raw` has accumulated raw data
        // we need to take the first the size of `episodeThisIter` to get the episode reward variables
        // and the raw data has the number of reward variable for each episode
        // for example, let's say we have two rows and this model has 3 reward variables
        // 1) iter=1, episode_this_iter=2, metrics_raw=[1.1, 1.2, 1.3, 2.1, 2.2, 2.3]
        // 2) iter=2, episode_this_iter=3, metrics_raw=[11.1 11.2 11.3, 12.1, 12.2, 12.3, 13.1, 13.2, 13.3, 1.1, 1.2, 1.3, 2.1, 2.2, 2.3]
        List<Double> metircsRawData =
            Arrays.asList(rawDataString
                .replace("[", "").replace("]", "")
                .split(",", episodesThisIter * numReward + 1)).subList(0, episodesThisIter * numReward ).stream()
                .map(Double::valueOf)
                .collect(Collectors.toList());

        List<List<MetricsRawThisEpisode>> episodeRaw = new ArrayList<>();
        for (int i = 0; i < metircsRawData.size(); i += numReward) {
            List<MetricsRawThisEpisode> metricsRawThisEpisodes = new ArrayList<>();
            for (int idx = 0; idx < numReward; idx++) {
                metricsRawThisEpisodes.add(new MetricsRawThisEpisode(idx, metircsRawData.get(i + idx)));
            }
            episodeRaw.add(metricsRawThisEpisodes);
        }
        return episodeRaw;
    }
}
