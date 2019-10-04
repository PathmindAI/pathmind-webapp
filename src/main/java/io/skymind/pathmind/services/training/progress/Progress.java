package io.skymind.pathmind.services.training.progress;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Progress {
    private String id;
    private String algorithm;
    private Map<String, String> hyperParameters;
    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;

    private List<RewardScore> rewardProgression;

    // for deserialization
    Progress(){}

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

    public LocalDateTime getStoppedAt() {
        return stoppedAt;
    }

    public void setStoppedAt(LocalDateTime stoppedAt) {
        this.stoppedAt = stoppedAt;
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
