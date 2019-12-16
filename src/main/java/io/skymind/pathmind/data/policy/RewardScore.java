package io.skymind.pathmind.data.policy;

public class RewardScore {
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
