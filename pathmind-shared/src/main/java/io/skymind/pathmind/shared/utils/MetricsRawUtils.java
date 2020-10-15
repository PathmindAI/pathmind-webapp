package io.skymind.pathmind.shared.utils;

import io.skymind.pathmind.shared.data.MetricsRaw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MetricsRawUtils {
    private MetricsRawUtils()  {
        throw new IllegalAccessError("Static usage only");
    }

    public static List<MetricsRaw> toMetricsRaw(String rawDataString, int iteration, int episodesThisIter, int numReward, int numAgents) {
        // since `hist_stats/metrics_raw` has accumulated raw data
        // we need to take the first the size of `episodeThisIter` to get the episode reward variables
        // and the raw data has the number of reward variable for each episode
        // for example, let's say we have two rows and this model has 3 reward variables
        // 1) iter=1, episode_this_iter=2, metrics_raw=[1.1, 1.2, 1.3, 2.1, 2.2, 2.3]
        // 2) iter=2, episode_this_iter=3, metrics_raw=[11.1 11.2 11.3, 12.1, 12.2, 12.3, 13.1, 13.2, 13.3, 1.1, 1.2, 1.3, 2.1, 2.2, 2.3]
        List<Double> metircsRawData =
            Arrays.asList(rawDataString
                .replace("[", "").replace("]", "")
                .split(",", numAgents * episodesThisIter * numReward + 1)).subList(0, numAgents * episodesThisIter * numReward ).stream()
                .map(Double::valueOf)
                .collect(Collectors.toList());

        List<MetricsRaw> result = new ArrayList<>();

        int indexOfList = 0;
        for (int episode = 0; episode < episodesThisIter; episode++) {
            for (int agent = 0; agent < numAgents; agent++) {
                for (int idx = 0; idx < numReward; idx++) {
                    result.add(new MetricsRaw(agent, iteration, episode, idx, metircsRawData.get(indexOfList++)));
                }
            }
        }

        return result;
    }

}
