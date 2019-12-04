package io.skymind.pathmind.data.policy;

public class RewardScore {
    // May be NaN!
    private double max;
    private double min;
    private double mean;

    private int iteration;

    // for deserialization
    private RewardScore(){}

    public RewardScore(double max, double min, double mean, int iteration) {
        this.max = max;
        this.min = min;
        this.mean = mean;
        this.iteration = iteration;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }
}
