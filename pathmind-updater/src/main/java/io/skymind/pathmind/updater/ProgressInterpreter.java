package io.skymind.pathmind.updater;

import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardScore;
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

@Slf4j
public class ProgressInterpreter {
    enum  RAY_PROGRESS {
        EPISODE_REWARD_MAX("episode_reward_max"),
        EPISODE_REWARD_MIN("episode_reward_min"),
        EPISODE_REWARD_MEAN("episode_reward_mean"),
        EPISODES_THIS_ITER("episodes_this_iter"),
        TRAINING_ITERATION("training_iteration");

        String column;

        RAY_PROGRESS(String column) {
            this.column = column;
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
        return interpret(entry, null);
    }

    public static Policy interpret(Map.Entry<String, String> entry, List<RewardScore> previousScores){
        final Policy policy = interpretKey(entry.getKey());
        interpretScores(entry, previousScores, policy);
        return policy;
    }

    private static void interpretScores(Map.Entry<String, String> entry, List<RewardScore> previousScores, Policy policy) {
        final List<RewardScore> scores = previousScores == null || previousScores.size() == 0 ? new ArrayList<>() : previousScores;
        final int lastIteration = scores.size() == 0 ? -1 : scores.get(scores.size() - 1).getIteration();

        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        settings.selectFields(RAY_PROGRESS.values());

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
}