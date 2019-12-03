package io.skymind.pathmind.data.policy;

import com.fasterxml.jackson.annotation.JsonProperty;

// TODO -> Should eventually be moved to a policy package with the Policy class. Every major data model object should
// be moved to it's own separate project with their accompanying sub data model objects.
public class HyperParameters
{
    public static final String LEARNING_RATE = "lr";
    public static final String GAMMA = "gamma";
    public static final String BATCH_SIZE = "sgd_minibatch_size";

    @JsonProperty("lr")
    private double learningRate;
    @JsonProperty("gamma")
    private double gamma;
    @JsonProperty("sgd_minibatch_size")
    private int batchSize;

    public HyperParameters() {
    }

    public HyperParameters(double learningRate, double gamma, int batchSize) {
        this.learningRate = learningRate;
        this.gamma = gamma;
        this.batchSize = batchSize;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}