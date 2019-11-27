package io.skymind.pathmind.services.training.progress;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.StringReader;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ProgressInterpreter {

    private static Logger log = LogManager.getLogger(ProgressInterpreter.class);
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd_HH-mm-ss");

    private static final int TRIAL_ID_LEN = 8;
    private static final int DATE_LEN = 19;

    public static Progress interpretKey(String keyString) {
        final Progress progress = new Progress();
        progress.setId(keyString);

        final HashMap<String, String> hyperParameters = new HashMap<>();

        StringBuilder buffer = new StringBuilder();
        // looks something like this:
        // PPO_CoffeeEnvironment_0_gamma=0.99,lr=5e-05,sgd_minibatch_size=128_2019-08-05_13-56-455cdir_3f

        int keyLength = keyString.length();
        String id = keyString.substring(keyLength - TRIAL_ID_LEN);
        String dateTime = keyString.substring(keyLength - TRIAL_ID_LEN - DATE_LEN, keyLength - TRIAL_ID_LEN);
        keyString = keyString.substring(0, keyLength - TRIAL_ID_LEN - DATE_LEN - 1);

        // keyString now looks like :
        // PPO_CoffeeEnvironment_0_gamma=0.99,lr=5e-05,sgd_minibatch_size=128
        List<String> list = Arrays.asList(keyString.split("_", 4));

        progress.setAlgorithm(list.get(0));

        Arrays.stream(list.get(3).split(",")).forEach(it -> {
            final String[] split = it.split("=");
            hyperParameters.put(split[0], split[1]);
        });
        progress.setHyperParameters(hyperParameters);

        try {
            final LocalDateTime utcTime = LocalDateTime.parse(dateTime, dateFormat);
            final LocalDateTime time = ZonedDateTime.ofInstant(utcTime.toInstant(ZoneOffset.UTC), Clock.systemDefaultZone().getZone()).toLocalDateTime();
            progress.setStartedAt(time);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        return progress;
    }


    public static Progress interpret(Map.Entry<String, String> entry){
        return interpret(entry, null);
    }

    public static Progress interpret(Map.Entry<String, String> entry, List<RewardScore> previousScores){
        final Progress progress = interpretKey(entry.getKey());

        final List<RewardScore> scores = previousScores == null || previousScores.size() == 0 ? new ArrayList<>() : previousScores;
        final int lastIteration = scores.size() == 0 ? -1 : scores.get(scores.size() - 1).getIteration();

        try (CSVReader reader = new CSVReader(new StringReader(entry.getValue()))) {

            String[] record = null;
            //skip header row
            reader.readNext();

            while((record = reader.readNext()) != null){
                final int iter = Integer.parseInt(record[9]); // training_iteration

                if (iter > lastIteration) {
                    final String max = record[0];   // episode_reward_max
                    final String min = record[1];   // episode_reward_min
                    final String mean = record[2];  // episode_reward_mean

                    scores.add(new RewardScore(
                            Double.valueOf(max.equals("nan") ? "NaN" : max),
                            Double.valueOf(min.equals("nan") ? "NaN" : min),
                            Double.valueOf(mean.equals("nan") ? "NaN" : mean),
                            iter
                    ));
                }
            }
            progress.setRewardProgression(scores);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }


        return progress;
    }


}
