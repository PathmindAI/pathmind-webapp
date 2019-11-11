package io.skymind.pathmind.services.training.progress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

public class ProgressInterpreter {

    private static Logger log = LogManager.getLogger(ProgressInterpreter.class);
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd_HH-mm-ss");

    public static Progress interpretKey(String keyString) {
        final Progress progress = new Progress();
        progress.setId(keyString);

        final HashMap<String, String> hyperParameters = new HashMap<>();

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
                    progress.setAlgorithm(buffer.toString());
                    buffer.setLength(0);
                }else if(!envName){
                    envName = true;
                    buffer.setLength(0);
                }else if(!runCounter){
                    runCounter = true;
                    buffer.setLength(0);
                }
                lastFoundIdx = i;
            } else if(Character.isDigit(cur) && lastFoundIdx == i - 1 && alg && envName && runCounter && !params){
                params = true;
                String parameters = buffer.toString();
                parameters = parameters.substring(1, parameters.length() - 1);
                Arrays.stream(parameters.split(",")).forEach(it -> {
                    final String[] split = it.split("=");
                    hyperParameters.put(split[0], split[1]);
                });
                progress.setHyperParameters(Collections.unmodifiableMap(hyperParameters));
                buffer.setLength(0);
            }
            buffer.append(cur);
        }

        try {
            final String dateTime = buffer.toString().substring(0, 19);
            final LocalDateTime utcTime = LocalDateTime.parse(dateTime, dateFormat);
            final LocalDateTime time = ZonedDateTime.ofInstant(utcTime.toInstant(ZoneOffset.UTC), Clock.systemDefaultZone().getZone()).toLocalDateTime();
            progress.setStartedAt(time);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        return progress;
    }

    public static Progress interpret(Map.Entry<String, String> entry){
        final Progress progress = interpretKey(entry.getKey());

        try {
            final CsvMapReader mapReader = new CsvMapReader(new StringReader(entry.getValue()), CsvPreference.STANDARD_PREFERENCE);
            final String[] header = mapReader.getHeader(true);

            final ArrayList<RewardScore> scores = new ArrayList<>();
            Map<String, String> map;
            while( (map = mapReader.read(header)) != null ) {
                final String max = map.get("episode_reward_max");
                final String min = map.get("episode_reward_min");
                final String mean = map.get("episode_reward_mean");
                scores.add(new RewardScore(
                        Double.valueOf(max.equals("nan") ? "NaN" : max),
                        Double.valueOf(min.equals("nan") ? "NaN" : min),
                        Double.valueOf(mean.equals("nan") ? "NaN" : mean),
                        Integer.parseInt(map.get("training_iteration"))
                ));
            }
            progress.setRewardProgression(scores);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return progress;
    }

}
