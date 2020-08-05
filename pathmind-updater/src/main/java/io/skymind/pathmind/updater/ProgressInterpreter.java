package io.skymind.pathmind.updater;

import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import io.skymind.pathmind.shared.data.*;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ProgressInterpreter {
    enum  RAY_PROGRESS {
        EPISODE_REWARD_MAX("episode_reward_max"),
        EPISODE_REWARD_MIN("episode_reward_min"),
        EPISODE_REWARD_MEAN("episode_reward_mean"),
        EPISODES_THIS_ITER("episodes_this_iter"),
        TRAINING_ITERATION("training_iteration"),
        METRICS_RAW("hist_stats/metrics_raw");

        String column;

        RAY_PROGRESS(String column) {
            this.column = column;
        }

        static String[] metricsColumns(int numReward) {
            List<RAY_PROGRESS> metricsColumns = List.of(TRAINING_ITERATION);
            List<String> columns = metricsColumns.stream().map(e -> e.column).collect(Collectors.toList());
            for (int i = 0; i < numReward; i++) {
                columns.add(metricsMax(i));
                columns.add(metricsMin(i));
                columns.add(metricsMean(i));
            }

            return columns.toArray(new String[0]);
        }

        static RAY_PROGRESS[] scoreColumns() {
            List<RAY_PROGRESS> scoreColumns = List.of(EPISODE_REWARD_MAX, EPISODE_REWARD_MIN, EPISODE_REWARD_MEAN, EPISODES_THIS_ITER, TRAINING_ITERATION);
            return scoreColumns.toArray(new RAY_PROGRESS[0]);
        }

        static String[] metricsRawColumns() {
            List<RAY_PROGRESS> scoreColumns = List.of(EPISODES_THIS_ITER, TRAINING_ITERATION, METRICS_RAW);
            List<String> columns = scoreColumns.stream().map(e -> e.column).collect(Collectors.toList());
            return columns.toArray(new String[0]);
        }

        static final String metrics_max = "custom_metrics/metrics_%d_max";
        static final String metrics_min = "custom_metrics/metrics_%d_min";
        static final String metrics_mean = "custom_metrics/metrics_%d_mean";

        static String metricsMax(int index) {
            return String.format(metrics_max, index);
        }

        static String metricsMin(int index) {
            return String.format(metrics_min, index);
        }

        static String metricsMean(int index) {
            return String.format(metrics_mean, index);
        }
    }

    private static final int ALGORITHM = 0;
    private static final int NAME = 2;

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd_HH-mm-ss");

    private static final int TRIAL_ID_LEN = 8;
    private static final int DATE_LEN = 19;

    public static Policy interpretKey(String keyString) {
        final Policy policy = new Policy();
        policy.setExternalId(keyString);

        int keyLength = keyString.length();
        String dateTime = null;

        // looks something like this:
        // PPO_PathmindEnvironment_0_clip_param=0.2,entropy_coeff=0.035,gamma=0.94978,kl_coeff=0.3,kl_target=0.03,lambda=0.96,lr=0.0016037,nu_2020-02-12_22-16-07ix9qrg3i
        dateTime = keyString.substring(keyLength - TRIAL_ID_LEN - DATE_LEN, keyLength - TRIAL_ID_LEN);
        keyString = keyString.substring(0, keyLength - TRIAL_ID_LEN - DATE_LEN - 1);

        // keyString now looks like :
        // PPO_PathmindEnvironment_0_clip_param=0.2,entropy_coeff=0.035,gamma=0.94978,kl_coeff=0.3,kl_target=0.03,lambda=0.96,lr=0.0016037,nu
        List<String> list = Arrays.asList(keyString.split("_", 4));

        policy.setName(list.get(NAME));

        try {
            if (dateTime != null) {
                final LocalDateTime utcTime = LocalDateTime.parse(dateTime, dateFormat);
                final LocalDateTime time = ZonedDateTime.ofInstant(utcTime.toInstant(ZoneOffset.UTC), Clock.systemDefaultZone().getZone()).toLocalDateTime();
                policy.setStartedAt(time);
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        return policy;
    }

    public static Policy interpret(Map.Entry<String, String> entry){
        return interpret(entry, null, null, 0);
    }

    public static Policy interpret(Map.Entry<String, String> entry, List<RewardScore> previousScores,
                                   List<Metrics> previousMetrics, int numReward){
        final Policy policy = interpretKey(entry.getKey());
        interpretScores(entry, previousScores, policy);
        interpretMetrics(entry, previousMetrics, policy, numReward);
        return policy;
    }

    private static void interpretScores(Map.Entry<String, String> entry, List<RewardScore> previousScores, Policy policy) {
        final List<RewardScore> scores = previousScores == null || previousScores.size() == 0 ? new ArrayList<>() : previousScores;
        final int lastIteration = scores.size() == 0 ? -1 : scores.get(scores.size() - 1).getIteration();

        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        settings.selectFields(RAY_PROGRESS.scoreColumns());
        settings.getFormat().setLineSeparator("\n");

        CsvParser parser = new CsvParser(settings);
        List<Record> allRecords = parser.parseAllRecords(new ByteArrayInputStream(entry.getValue().getBytes()));

        for(Record record : allRecords){
            final Integer iteration = record.getInt(RAY_PROGRESS.TRAINING_ITERATION);

            if (iteration > lastIteration) {
                final String max = record.getString(RAY_PROGRESS.EPISODE_REWARD_MAX);
                final String min = record.getString(RAY_PROGRESS.EPISODE_REWARD_MIN);
                final String mean = record.getString(RAY_PROGRESS.EPISODE_REWARD_MEAN);
                final Integer episodeCount = record.getInt(RAY_PROGRESS.EPISODES_THIS_ITER);

                scores.add(new RewardScore(
                    Double.valueOf(max.equals("nan") ? "NaN" : max),
                    Double.valueOf(min.equals("nan") ? "NaN" : min),
                    Double.valueOf(mean.equals("nan") ? "NaN" : mean),
                    iteration,
                    episodeCount
                ));
            }
            policy.setScores(scores);
        }
    }

    private static void interpretMetrics(Map.Entry<String, String> entry, List<Metrics> previousMetrics, Policy policy, int numReward) {
        final List<Metrics> metrics = previousMetrics == null || previousMetrics.size() == 0 ? new ArrayList<>() : previousMetrics;
        final int lastIteration = metrics.size() == 0 ? -1 : metrics.get(metrics.size() - 1).getIteration();

        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        settings.selectFields((RAY_PROGRESS.metricsColumns(numReward)));

        CsvParser parser = new CsvParser(settings);
        List<Record> allRecords = parser.parseAllRecords(new ByteArrayInputStream(entry.getValue().getBytes()));

        for(Record record : allRecords) {
            // missing information check
            if (Arrays.asList(record.getValues()).contains(null)) {
                log.debug("There are missing information in the csv contents");
                continue;
            }

            final Integer iteration = record.getInt(RAY_PROGRESS.TRAINING_ITERATION);

            if (iteration > lastIteration) {
                List<MetricsThisIter> metricsThisIter = new ArrayList<>();
                for (int idx = 0; idx < numReward; idx++) {
                    final String max = record.getString(RAY_PROGRESS.metricsMax(idx));
                    final String min = record.getString(RAY_PROGRESS.metricsMin(idx));
                    final String mean = record.getString(RAY_PROGRESS.metricsMean(idx));
                    metricsThisIter.add(new MetricsThisIter(
                        idx,
                        Double.valueOf(max.equals("nan") ? "NaN" : max),
                        Double.valueOf(min.equals("nan") ? "NaN" : min),
                        Double.valueOf(mean.equals("nan") ? "NaN" : mean)
                    ));
                }
                metrics.add(new Metrics(iteration, metricsThisIter));
            }
            policy.setMetrics(metrics);
        }
    }

    public static void interpretMetricsRaw(Map.Entry<String, String> entry, Policy policy, List<MetricsRaw> previousMetricsRaw, int startIteration,  int numReward) {
        List<MetricsRaw> metricsRaws = previousMetricsRaw == null || previousMetricsRaw.size() == 0 ? new ArrayList<>() : previousMetricsRaw;
        final int lastIteration = metricsRaws.size() == 0 ? Math.max(startIteration, 0) : metricsRaws.get(metricsRaws.size() - 1).getIteration();;

        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        settings.setMaxCharsPerColumn(-1);
        settings.selectFields((RAY_PROGRESS.metricsRawColumns()));

        CsvParser parser = new CsvParser(settings);
        List<Record> allRecords = parser.parseAllRecords(new ByteArrayInputStream(entry.getValue().getBytes()));

        for(Record record : allRecords) {
            // missing information check
            if (Arrays.asList(record.getValues()).contains(null)) {
                log.debug("There are missing information in the csv contents");
                continue;
            }

            final Integer iteration = record.getInt(RAY_PROGRESS.TRAINING_ITERATION);

            if (iteration > lastIteration) {
                final int episodesThisIter = record.getInt(RAY_PROGRESS.EPISODES_THIS_ITER);

                // since `hist_stats/metrics_raw` has accumulated raw data
                // we need to take the first the size of `episodeThisIter` to get the episode reward variables
                // and the raw data has the number of reward variable for each episode
                // for example, let's say we have two rows and this model has 3 reward variables
                // 1) iter=1, episode_this_iter=2, metrics_raw=[1.1, 1.2, 1.3, 2.1, 2.2, 2.3]
                // 2) iter=2, episode_this_iter=3, metrics_raw=[11.1 11.2 11.3, 12.1, 12.2, 12.3, 13.1, 13.2, 13.3, 1.1, 1.2, 1.3, 2.1, 2.2, 2.3]
                List<Double> metircsRawData =
                    Arrays.asList(record.getString(RAY_PROGRESS.METRICS_RAW.column)
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

                metricsRaws.add(new MetricsRaw(iteration, episodeRaw));
            }
        }
        policy.setMetricsRaws(metricsRaws);
    }
}