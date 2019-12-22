package io.skymind.pathmind.services.training.progress;

import com.opencsv.CSVReader;
import io.skymind.pathmind.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.policy.RewardScore;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.data.utils.RunUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.StringReader;
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
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd_HH-mm-ss");

    private static final int TRIAL_ID_LEN = 8;
    private static final int DATE_LEN = 19;

    public static Policy interpretKey(String keyString) {
        final Policy policy = new Policy();
        policy.setExternalId(keyString);

        int keyLength = keyString.length();
        String dateTime = null;

        if (keyString.endsWith(RunUtils.TEMPORARY_POSTFIX)) {
            // looks something like this:
            // PPO_PathmindEnvironment_0_gamma=0.99,lr=1.0E-5,sgd_minibatch_size=128_1TEMP
            keyString = keyString.substring(0, keyLength - RunUtils.TEMPORARY_POSTFIX.length() - 2);
        } else {
            // looks something like this:
            // PPO_CoffeeEnvironment_0_gamma=0.99,lr=5e-05,sgd_minibatch_size=128_2019-08-05_13-56-455cdir_3f
            dateTime = keyString.substring(keyLength - TRIAL_ID_LEN - DATE_LEN, keyLength - TRIAL_ID_LEN);
            keyString = keyString.substring(0, keyLength - TRIAL_ID_LEN - DATE_LEN - 1);
        }

        // keyString now looks like :
        // PPO_CoffeeEnvironment_0_gamma=0.99,lr=5e-05,sgd_minibatch_size=128
        List<String> list = Arrays.asList(keyString.split("_", 4));

        policy.setAlgorithm(list.get(0));

        Arrays.stream(list.get(3).split(",")).forEach(it -> {
            final String[] split = it.split("=");
            setHyperParameter(policy, split[0], split[1]);
        });

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

    private static void setHyperParameter(Policy policy, String name, String value) {
        switch (name) {
            case PolicyUtils.LEARNING_RATE:
                policy.setLearningRate(Double.valueOf(value));
                break;
            case PolicyUtils.GAMMA:
                policy.setGamma(Double.valueOf(value));
                break;
            case PolicyUtils.BATCH_SIZE:
                policy.setBatchSize(Integer.valueOf(value));
                break;
        }
    }

    public static Policy interpret(Map.Entry<String, String> entry){
        final Policy policy = interpretKey(entry.getKey());

        final ArrayList<RewardScore> scores = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new StringReader(entry.getValue()))) {

            String[] record = null;
            //skip header row
            reader.readNext();

            while((record = reader.readNext()) != null){
                final String max = record[0];   // episode_reward_max
                final String min = record[1];   // episode_reward_min
                final String mean = record[2];  // episode_reward_mean

                scores.add(new RewardScore(
                        Double.valueOf(max.equals("nan") ? "NaN" : max),
                        Double.valueOf(min.equals("nan") ? "NaN" : min),
                        Double.valueOf(mean.equals("nan") ? "NaN" : mean),
                        Integer.parseInt(record[9]) // training_iteration
                ));
            }
            policy.setScores(scores);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return policy;
    }
}