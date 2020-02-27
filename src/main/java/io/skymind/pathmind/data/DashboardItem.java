package io.skymind.pathmind.data;

import java.time.LocalDateTime;
import java.util.Objects;

import io.skymind.pathmind.constants.RunStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;

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
	private int iterationsProcessed;

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

	public int getIterationsProcessed() {
		return iterationsProcessed;
	}

	public void setIterationsProcessed(int iterationsProcessed) {
		this.iterationsProcessed = iterationsProcessed;
	}

	@Override
 	public boolean equals(Object o) {
 		if(this == o) return true;
 		if(o == null || getClass() != o.getClass()) return false;
 		DashboardItem item = (DashboardItem) o;
 		return Objects.equals(item.getProject(), getProject()) 
 				&& Objects.equals(item.getModel(), getModel())
 				&& Objects.equals(item.getExperiment(), getExperiment());
 	}

 	@Override
 	public int hashCode() {
 		return Objects.hash(getProject(), getModel(), getExperiment());
 	}

 	public boolean hasTrainingInProgress() {
		return this.getExperiment() != null && this.getLatestRun() != null && RunStatus.isRunning(this.getLatestRun().getStatusEnum());
	}
}
