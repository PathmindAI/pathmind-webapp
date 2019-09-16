package io.skymind.pathmind.services.training;

import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ProgressInterpreter {
    public static Progress interpret(Map.Entry<String, String> entry){
        final Progress progress = new Progress();
        final HashMap<String, String> hyperParameters = new HashMap<>();

        StringBuilder buffer = new StringBuilder();
        // looks something like this:
        // PPO_CoffeeEnvironment_0_gamma=0.99,lr=5e-05,sgd_minibatch_size=128_2019-08-05_13-56-455cdir_3f
        final char[] key = entry.getKey().toCharArray();

        boolean alg = false;
        boolean envName = false;
        boolean runCounter = false;
        boolean params = false;

        int lastFoundIdx = 0;
        for (int i = 0; i < key.length; i++) {
            final char cur = key[i];
            if(cur == '_'){
                if(!alg){
                    alg = true;
                    progress.setAlgorithm(buffer.toString());
                    buffer = new StringBuilder();
                }else if(!envName){
                    envName = true;
                    buffer = new StringBuilder();
                }else if(!runCounter){
                    runCounter = true;
                    buffer = new StringBuilder();
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
                buffer = new StringBuilder();
            }
            buffer.append(cur);
        }
        final String dateTime = buffer.toString().substring(0, 19);
        final LocalDateTime time = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("uuuu-MM-dd_HH-mm-ss"));
        progress.setStartedAt(time);
        progress.setId(buffer.substring(20));


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

    public static class Progress {
        private String id;
        private String algorithm;
        private Map<String, String> hyperParameters;
        private LocalDateTime startedAt;

        private List<RewardScore> rewardProgression;

        // for deserialization
        private Progress(){}

        public Progress(String id, String algorithm, Map<String, String> hyperParameters, LocalDateTime startedAt, List<RewardScore> rewardProgression) {
            this.id = id;
            this.algorithm = algorithm;
            this.hyperParameters = hyperParameters;
            this.startedAt = startedAt;
            this.rewardProgression = rewardProgression;
        }

        public String getAlgorithm() {
            return algorithm;
        }

        public void setAlgorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        public Map<String, String> getHyperParameters() {
            return hyperParameters;
        }

        public void setHyperParameters(Map<String, String> hyperParameters) {
            this.hyperParameters = hyperParameters;
        }

        public LocalDateTime getStartedAt() {
            return startedAt;
        }

        public void setStartedAt(LocalDateTime startedAt) {
            this.startedAt = startedAt;
        }

        public List<RewardScore> getRewardProgression() {
            return rewardProgression;
        }

        public void setRewardProgression(List<RewardScore> rewardProgression) {
            this.rewardProgression = rewardProgression;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class RewardScore {
        // May be NaN!
        private Double max;
        private Double min;
        private Double mean;

        private Integer iteration;

        // for deserialization
        private RewardScore(){}

        public RewardScore(Double max, Double min, Double mean, Integer iteration) {
            this.max = max;
            this.min = min;
            this.mean = mean;
            this.iteration = iteration;
        }

        public Double getMax() {
            return max;
        }

        public void setMax(Double max) {
            this.max = max;
        }

        public Double getMin() {
            return min;
        }

        public void setMin(Double min) {
            this.min = min;
        }

        public Double getMean() {
            return mean;
        }

        public void setMean(Double mean) {
            this.mean = mean;
        }

        public Integer getIteration() {
            return iteration;
        }

        public void setIteration(Integer iteration) {
            this.iteration = iteration;
        }
    }
}
