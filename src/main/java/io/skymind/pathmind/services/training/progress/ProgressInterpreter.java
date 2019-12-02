package io.skymind.pathmind.services.training.progress;

import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.policy.HyperParameters;
import io.skymind.pathmind.data.policy.RewardScore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.StringReader;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ProgressInterpreter
{
    private static Logger log = LogManager.getLogger(ProgressInterpreter.class);
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd_HH-mm-ss");

    public static Policy interpretKey(String keyString) {
        final Policy policy = new Policy();
        policy.setExternalId(keyString);

        StringBuilder buffer = new StringBuilder();
        // looks something like this:
        // PPO_CoffeeEnvironment_0_gamma=0.99,lr=5e-05,sgd_minibatch_size=128_2019-08-05_13-56-455cdir_3f

        final char[] key = keyString.toCharArray();

        boolean alg = false;
        boolean envName = false;
        boolean runCounter = false;
        boolean params = false;

        // PERFORMANCE -> Can we minimize this for our needs since it's so expensive... Do we need to parse for the algo? Is there a quick way to just get the hyperParams
        // as that seems to eb all we ever end up using from this parsing...
        int lastFoundIdx = 0;
        for (int i = 0; i < key.length; i++) {
            final char cur = key[i];
            if(cur == '_'){
                if(!alg){
                    alg = true;
                    policy.setAlgorithm(buffer.toString());
                    buffer.setLength(0);
                }else if(!envName){
                    envName = true;
                    buffer.setLength(0);
                }else if(!runCounter){
                    runCounter = true;
                    buffer.setLength(0);
                }
                lastFoundIdx = i;
            // IMPORTANT PERFORMANCE -> It's very important that Character.isDigit(cur) is last because this is a code hotspot.
            } else if(lastFoundIdx == i - 1 && alg && envName && runCounter && !params && Character.isDigit(cur)){
                params = true;
                String parameters = buffer.substring(1, buffer.length() - 1);
                Arrays.stream(parameters.split(",")).forEach(it -> {
                    final String[] split = it.split("=");
                    setHyperParameter(policy, split[0], split[1]);
                });
                buffer.setLength(0);
            }
            buffer.append(cur);
        }

        try {
            final String dateTime = buffer.toString().substring(0, 19);
            final LocalDateTime utcTime = LocalDateTime.parse(dateTime, dateFormat);
            final LocalDateTime time = ZonedDateTime.ofInstant(utcTime.toInstant(ZoneOffset.UTC), Clock.systemDefaultZone().getZone()).toLocalDateTime();
            policy.setStartedAt(time);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        return policy;
    }

    private static void setHyperParameter(Policy policy, String name, String value) {
        switch (name) {
            case HyperParameters.LEARNING_RATE:
                policy.getHyperParameters().setLearningRate(Double.valueOf(value));
                break;
            case HyperParameters.GAMMA:
                policy.getHyperParameters().setGamma(Double.valueOf(value));
                break;
            case HyperParameters.BATCH_SIZE:
                policy.getHyperParameters().setBatchSize(Integer.valueOf(value));
                break;
        }
    }

    public static Policy interpret(Map.Entry<String, String> entry)
    {
        final Policy policy = interpretKey(entry.getKey());

        try(StringReader stringReader = new StringReader(entry.getValue());
            CsvMapReader mapReader = new CsvMapReader(stringReader, CsvPreference.STANDARD_PREFERENCE))
        {
            mapReader.getHeader(true);
            final String[] header = new String[]{"episode_reward_max", "episode_reward_min", "episode_reward_mean", null, null, null, null, null, null, "training_iteration", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
            final CellProcessor[] processors = new CellProcessor[] {new NotNull(), new NotNull(), new NotNull(), null, null, null, null, null, null, new NotNull(), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};

            final ArrayList<RewardScore> scores = new ArrayList<>();
            Map<String, Object> map;
            while( (map = mapReader.read(header, processors)) != null ) {
                final String max = map.get("episode_reward_max").toString();
                final String min = map.get("episode_reward_min").toString();
                final String mean = map.get("episode_reward_mean").toString();
                scores.add(new RewardScore(
                        Double.valueOf(max.equals("nan") ? "NaN" : max),
                        Double.valueOf(min.equals("nan") ? "NaN" : min),
                        Double.valueOf(mean.equals("nan") ? "NaN" : mean),
                        Integer.parseInt(map.get("training_iteration").toString())
                ));
            }
            policy.setScores(scores);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return policy;
    }
}