package io.skymind.pathmind.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.data.policy.RewardScore;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

// The @JsonIgnoreProperties will only be here until I have finished moving the json code out of the database.
@JsonIgnoreProperties(value = { "hyperParameters" })
public class Policy extends Data
{
	@JsonIgnore
	private long runId;
	@JsonProperty("id")
	private String externalId;
	@JsonIgnore
	private String progress;
	@JsonIgnore
	private byte[] file;
	@JsonIgnore
	private byte[] snapshot;

    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;

	@JsonIgnore
	private double learningRate;
	@JsonIgnore
	private double gamma;
	@JsonIgnore
	private int batchSize;

    // For now this is hardcoded: https://github.com/SkymindIO/pathmind-webapp/issues/101
    private Algorithm algorithm = Algorithm.PPO;

    // REFACTOR -> Same as Progress which is not saved to the database and is parsed back and forth...
    @JsonProperty("rewardProgression")
    private List<RewardScore> scores;

    // Helper GUI attributes not stored in the database
	@JsonIgnore
	private Project project;
	@JsonIgnore
	private Model model;
	@JsonIgnore
	private Experiment experiment;
	@JsonIgnore
	private Run run;

	// Helper for now for performance reasons.
	@JsonIgnore
	private String parsedName;

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

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public byte[] getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(byte[] snapshot) {
		this.snapshot = snapshot;
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

	@JsonIgnore
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

	public String getParsedName() {
		return parsedName;
	}

	public void setParsedName(String parsedName) {
		this.parsedName = parsedName;
	}

	// STEPH -> Clean this up when we add notes to the database.
	@JsonIgnore
	public String getNotes() {
		throw new RuntimeException("Should not be used");
	}

	public void setNotes(String notes) {
		// The notes are currently just the hyperParameters
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
}
