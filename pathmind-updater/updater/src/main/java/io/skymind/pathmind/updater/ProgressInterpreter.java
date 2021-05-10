package io.skymind.pathmind.updater;

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

import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import io.skymind.pathmind.shared.data.Metrics;
import io.skymind.pathmind.shared.data.MetricsRaw;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardScore;
import io.skymind.pathmind.shared.utils.MetricsRawUtils;
import io.skymind.pathmind.shared.utils.PathmindNumberUtils;
import lombok.extern.slf4j.Slf4j;

import static io.skymind.pathmind.shared.utils.PathmindNumberUtils.convertValidDouble;

@Slf4j
public class ProgressInterpreter {

    public static final int MAX_COLUMNS = Integer.MAX_VALUE;

    enum RAY_PROGRESS {
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

        static String[] metricsColumns(int numReward, int numAgents) {
            List<String> columns = new ArrayList<>(Arrays.asList(TRAINING_ITERATION.column));
            for (int i = 0; i < numReward * numAgents; i++) {
                columns.add(metricsMax(i));
                columns.add(metricsMin(i));
                columns.add(metricsMean(i));
            }

            return columns.toArray(new String[0]);
        }

        static RAY_PROGRESS[] scoreColumns() {
            return new RAY_PROGRESS[]{EPISODE_REWARD_MAX, EPISODE_REWARD_MIN, EPISODE_REWARD_MEAN, EPISODES_THIS_ITER, TRAINING_ITERATION};
        }

        static String[] metricsRawColumns() {
            return new String[]{EPISODES_THIS_ITER.column, TRAINING_ITERATION.column, METRICS_RAW.column};
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

    public static Policy interpret(Map.Entry<String, String> entry, List<RewardScore> previousScores,
                                   List<Metrics> previousMetrics, int numReward, int numAgents) {
        final Policy policy = interpretKey(entry.getKey());
        interpretScores(entry, previousScores, policy);
        interpretMetrics(entry, previousMetrics, policy, numReward, numAgents);
        return policy;
    }

    private static void interpretScores(Map.Entry<String, String> entry, List<RewardScore> previousScores, Policy policy) {
        final List<RewardScore> scores = previousScores == null || previousScores.size() == 0 ? new ArrayList<>() : previousScores;
        final int lastIteration = scores.size() == 0 ? -1 : scores.get(scores.size() - 1).getIteration();

        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        settings.selectFields(RAY_PROGRESS.scoreColumns());
        settings.getFormat().setLineSeparator("\n");
        settings.setMaxColumns(MAX_COLUMNS);

        CsvParser parser = new CsvParser(settings);
        List<Record> allRecords = parser.parseAllRecords(new ByteArrayInputStream(entry.getValue().getBytes()));

        for (Record record : allRecords) {
            final Integer iteration = record.getInt(RAY_PROGRESS.TRAINING_ITERATION);

            if (iteration > lastIteration) {
                final String mean = record.getString(RAY_PROGRESS.EPISODE_REWARD_MEAN);
                final Integer episodeCount = record.getInt(RAY_PROGRESS.EPISODES_THIS_ITER);

                scores.add(new RewardScore(
                        convertValidDouble(mean),
                        iteration,
                        episodeCount
                ));
            }
            policy.setScores(scores);
        }
    }

    private static void interpretMetrics(Map.Entry<String, String> entry, List<Metrics> previousMetrics, Policy policy, int numReward, int numAgents) {
        final List<Metrics> metrics = previousMetrics == null || previousMetrics.size() == 0 ? new ArrayList<>() : previousMetrics;
        final int lastIteration = metrics.size() == 0 ? -1 : metrics.get(metrics.size() - 1).getIteration();

        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        settings.selectFields((RAY_PROGRESS.metricsColumns(numReward, numAgents)));
        settings.setMaxColumns(MAX_COLUMNS);

        CsvParser parser = new CsvParser(settings);
        List<Record> allRecords = parser.parseAllRecords(new ByteArrayInputStream(entry.getValue().getBytes()));

        for (Record record : allRecords) {
            // missing information check
            if (Arrays.asList(record.getValues()).contains(null)) {
                log.debug("There are missing information in the csv contents");
                continue;
            }

            final Integer iteration = record.getInt(RAY_PROGRESS.TRAINING_ITERATION);

            if (iteration > lastIteration) {
                for (int agent = 0; agent < numAgents; agent++) {
                    for (int idx = 0; idx < numReward; idx++) {
                        final String max = record.getString(RAY_PROGRESS.metricsMax((agent * numReward) + idx));
                        final String min = record.getString(RAY_PROGRESS.metricsMin((agent * numReward) + idx));
                        final String mean = record.getString(RAY_PROGRESS.metricsMean((agent * numReward) + idx));
                        metrics.add(new Metrics(agent, iteration, idx, convertValidDouble(max), convertValidDouble(min), convertValidDouble(mean)));
                    }
                }
            }
            policy.setMetrics(metrics);
        }
    }

    public static void interpretMetricsRaw(Map.Entry<String, String> entry, Policy policy, List<MetricsRaw> previousMetricsRaw, int startIteration, int numReward, int numAgents) {
        List<MetricsRaw> metricsRaws = previousMetricsRaw == null || previousMetricsRaw.size() == 0 ? new ArrayList<>() : previousMetricsRaw;
        final int lastIteration = metricsRaws.size() == 0 ? Math.max(startIteration, 0) : metricsRaws.get(metricsRaws.size() - 1).getIteration();
        ;

        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        // to enable auto expansion we can set -1, but it can cause OOM
        // so i set it 327840 for now
        // https://s3.console.aws.amazon.com/s3/object/dh-training-dynamic-files.pathmind.com/id1533/output/PPO_PathmindEnvironment_0_num_sgd_iter%253D20%252Csgd_minibatch_size%253D128%252Ctrain_batch_size%253D4000_2020-10-21_21-07-53dvdzg7i2/progress.csv?region=us-east-1&tab=overview
//        settings.setMaxCharsPerColumn(-1);
        settings.setMaxCharsPerColumn(327840);
        settings.selectFields((RAY_PROGRESS.metricsRawColumns()));
        settings.setMaxColumns(MAX_COLUMNS);

        CsvParser parser = new CsvParser(settings);
        List<Record> allRecords = parser.parseAllRecords(new ByteArrayInputStream(entry.getValue().getBytes()));

        metricsRaws.addAll(parseMetricsRaw(allRecords, lastIteration, numReward, numAgents));
        policy.setMetricsRaws(metricsRaws);
    }

    public static List<MetricsRaw> parseMetricsRaw(List<Record> allRecords, int lastIteration, int numReward, int numAgents) {
        List<MetricsRaw> metricsRaws = new ArrayList<>();
        for (Record record : allRecords) {
            // missing information check
            if (Arrays.asList(record.getValues()).contains(null)) {
                log.debug("There are missing information in the csv contents");
                continue;
            }

            final Integer iteration = record.getInt(RAY_PROGRESS.TRAINING_ITERATION);

            if (iteration > lastIteration) {
                int episodesThisIter = record.getInt(RAY_PROGRESS.EPISODES_THIS_ITER);
                String rawDataString = record.getString(RAY_PROGRESS.METRICS_RAW.column);
                metricsRaws.addAll(MetricsRawUtils.toMetricsRaw(rawDataString, iteration, episodesThisIter, numReward, numAgents));
            }
        }
        return metricsRaws;
    }
}