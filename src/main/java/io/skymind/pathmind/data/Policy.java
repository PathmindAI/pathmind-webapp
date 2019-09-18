package io.skymind.pathmind.data;

import io.skymind.pathmind.constants.Algorithm;

public class Policy implements Data
{
	private long id;
	private long runId;
	private String name;
	private int algorithm;
	private String score;
	private byte[] hyperParameters;
	private byte[] file;

	// Helper GUI attributes not stored in the database
	private String projectName;
	private String modelName;
	private String experimentName;

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRunId() {
		return runId;
	}

	public void setRunId(long runId) {
		this.runId = runId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(int algorithm) {
		this.algorithm = algorithm;
	}

	// TODO -> Implement JOOQ converter
	public Algorithm getAlgorithmEnum() {
		return Algorithm.getEnumFromValue(algorithm);
	}

	// TODO -> Implement JOOQ converter
	public void setAlgorithEnum(Algorithm algorithEnum) {
		this.algorithm = algorithEnum.getValue();
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public byte[] getHyperParameters() {
		return hyperParameters;
	}

	public void setHyperParameters(byte[] hyperParameters) {
		this.hyperParameters = hyperParameters;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getExperimentName() {
		return experimentName;
	}

	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}
}
