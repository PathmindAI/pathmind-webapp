package io.skymind.pathmind.data;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * DTO for dashboard purposes
 */
@AllArgsConstructor
@Builder
public class DashboardItem {
	private Project project;
	private Model model;
	private Experiment experiment;
	private Run latestRun;
	private LocalDateTime latestUpdateTime;
	/**
	 * Flag indicates if a policy for {@link DashboardItem#latestRun} was already exported by a user
	 */
	private boolean policyExported;

	public Project getProject() {
		return this.project;
	}

	public Model getModel() {
		return this.model;
	}

	public Experiment getExperiment() {
		return this.experiment;
	}

	public Run getLatestRun() {
		return this.latestRun;
	}

	public LocalDateTime getLatestUpdateTime() {
		return this.latestUpdateTime;
	}

	public boolean isPolicyExported() {
		return this.policyExported;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public void setLatestRun(Run latestRun) {
		this.latestRun = latestRun;
	}

	public void setLatestUpdateTime(LocalDateTime latestUpdateTime) {
		this.latestUpdateTime = latestUpdateTime;
	}

	public void setPolicyExported(boolean policyExported) {
		this.policyExported = policyExported;
	}
}
