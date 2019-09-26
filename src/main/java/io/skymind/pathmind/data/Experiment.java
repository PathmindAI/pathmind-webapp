package io.skymind.pathmind.data;

import java.time.LocalDateTime;
import java.util.List;

public class Experiment extends Data
{
	private long modelId;
	private String rewardFunction;
	private LocalDateTime dateCreated;
	private LocalDateTime lastActivityDate;

	// Helper GUI attributes not stored in the database
	private Project project;
	private Model model;
	private List<Policy> policies;

	public Experiment() {
	}

	public String getRewardFunction() {
		return rewardFunction;
	}

	public void setRewardFunction(String rewardFunction) {
		this.rewardFunction = rewardFunction;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public LocalDateTime getLastActivityDate() {
		return lastActivityDate;
	}

	public void setLastActivityDate(LocalDateTime lastActivityDate) {
		this.lastActivityDate = lastActivityDate;
	}

	public long getModelId() {
		return modelId;
	}

	public void setModelId(long modelId) {
		this.modelId = modelId;
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

	public List<Policy> getPolicies() {
		return policies;
	}

	public void setPolicies(List<Policy> policies) {
		this.policies = policies;
	}
}
