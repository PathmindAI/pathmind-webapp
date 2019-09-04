package io.skymind.pathmind.bus.data;

public class ProjectUpdateStatus
{
	// TODO => I'm not sure all that will be needed but I'm sure it will be a lot more involved because
	// in this setup there could easily be racing conditions.
	private long projectId;
	private int scoreValue;

	public ProjectUpdateStatus() {
	}

	public ProjectUpdateStatus(long projectId, int scoreValue) {
		this.projectId = projectId;
		this.scoreValue = scoreValue;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public int getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(int scoreValue) {
		this.scoreValue = scoreValue;
	}
}
