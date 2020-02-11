package io.skymind.pathmind.db.data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class Policy extends Data
{
	private long runId;
	private String externalId;

    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;

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

	public List<RewardScore> getScores() {
		return scores == null ? Collections.emptyList() : scores;
	}

	public void setScores(List<RewardScore> scores) {
		this.scores = scores;
	}

	public boolean hasFile() {
		return hasFile;
	}

	public void setHasFile(boolean hasFile) {
		this.hasFile = hasFile;
	}
}
