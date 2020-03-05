package io.skymind.pathmind.data;

import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.data.policy.RewardScore;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class Policy extends Data
{
	private long runId;
	private String externalId;

    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;

	private double learningRate;
	private double gamma;
	private int batchSize;

	private String notes;

    // For now this is hardcoded: https://github.com/SkymindIO/pathmind-webapp/issues/101
    private Algorithm algorithm = Algorithm.PPO;

    // REFACTOR -> Same as Progress which is not saved to the database and is parsed back and forth...
    private List<RewardScore> scores;

    private boolean hasFile;

    // Helper GUI attributes not stored in the database
	private Project project;
	private Model model;
	private Experiment experiment;
	private Run run;

	public long getRunId() {
		return runId;
	}

	public void setRunId(long runId) {
		this.runId = runId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public Run getRun() {
		return run;
	}

	public void setRun(Run run) {
		this.run = run;
	}

	public Algorithm getAlgorithmEnum() {
		return algorithm;
	}

	// TODO -> At some point we should remove all the getXxxEnum() versions and only have the getXxx() methods for all Enums.
	public String getAlgorithm() {
		return algorithm == null ? null : algorithm.name();
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = StringUtils.isEmpty(algorithm) ? null : Algorithm.valueOf(algorithm.toUpperCase());
	}

	public void setAlgorithmEnum(Algorithm algorithm) {
		this.algorithm = algorithm;
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<RewardScore> getScores() {
		return scores == null ? Collections.emptyList() : scores;
	}

	public void setScores(List<RewardScore> scores) {
		this.scores = scores;
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

	public boolean hasFile() {
		return hasFile;
	}

	public void setHasFile(boolean hasFile) {
		this.hasFile = hasFile;
	}
}
