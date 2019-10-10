package io.skymind.pathmind.data;

import io.skymind.pathmind.constants.Algorithm;

import java.util.ArrayList;
import java.util.List;

public class Policy extends Data
{
	private long runId;
	private String externalId;
	private String progress;
	private byte[] file;

	private ArrayList<Number> scores = new ArrayList<>();

	// Helper GUI attributes not stored in the database
	private Project project;
	private Model model;
	private Experiment experiment;
	private Run run;

	// For now this is hardcoded: https://github.com/SkymindIO/pathmind-webapp/issues/101
	private Algorithm algorithm = Algorithm.PPO;

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

	public List<Number> getScores() {
		return scores;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	@Override
	public String toString() {
		return "Policy{" +
				"id=" + getId() +
				", runId=" + runId +
				", name=" + getName() +
				'}';
	}
}
