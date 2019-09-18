package io.skymind.pathmind.data;

import io.skymind.pathmind.constants.TestRun;

import java.time.LocalDateTime;
import java.util.List;

public class Experiment implements Data
{
	private long id;
	private String name;
	private long duration;
	private LocalDateTime dateCreated;
	private String rewardFunction;
	private int testRun;
	private String notes = "Todo";

	private long modelId;

	private List<Run> runs;

	public Experiment() {
	}

	public Experiment(String name, LocalDateTime dateCreated, String rewardFunction, TestRun testRun, long modelId, String notes) {
		this.name = name;
		this.dateCreated = dateCreated;
		this.rewardFunction = rewardFunction;
		this.testRun = testRun.getValue();
		this.modelId = modelId;
		this.notes = notes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Run> getRuns() {
		return runs;
	}

	public void setRuns(List<Run> runs) {
		this.runs = runs;
	}

	public String getRewardFunction() {
		return rewardFunction;
	}

	public void setRewardFunction(String rewardFunction) {
		this.rewardFunction = rewardFunction;
	}

	public long getModelId() {
		return modelId;
	}

	public void setModelId(long modelId) {
		this.modelId = modelId;
	}

	@Override
	public String toString() {
		return "Experiment{" +
				"id=" + id +
				", name='" + name + '\'' +
				", date=" + dateCreated +
				", modelId=" + modelId +
				", rewardFunction='" + rewardFunction + '\'' +
				", runs=" + runs +
				'}';
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setTestRun(int testRun) {
		this.testRun = testRun;
	}

	public int getTestRun() {
		return testRun;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
}
