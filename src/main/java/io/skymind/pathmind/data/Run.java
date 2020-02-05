package io.skymind.pathmind.data;

import java.time.LocalDateTime;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;

public class Run extends Data
{
	private int runType;
	private long experimentId;
	private int status;
	private LocalDateTime startedAt;
	private LocalDateTime stoppedAt;
	private LocalDateTime notificationSentAt;
	private LocalDateTime exportedAt;
	private long errorId;

	// Helper attributes
	private Experiment experiment;
	private Model model;
	private Project project;

	public Run() {
	}

	public int getRunType() {
		return runType;
	}

	public void setRunType(int runType) {
		this.runType = runType;
	}

	/**
	 * TODO -> Convert to a JOOQ converter.
	 */
	public void setRunTypeEnum(RunType runTypeEnum) {
		this.runType = runTypeEnum.getValue();
	}

	/**
	 * TODO -> Convert to a JOOQ converter.
	 */
	public RunType getRunTypeEnum() {
		return RunType.getEnumFromValue(runType);
	}

	public long getExperimentId() {
		return experimentId;
	}

	public void setExperimentId(long experimentId) {
		this.experimentId = experimentId;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public RunStatus getStatusEnum() {
		return RunStatus.getEnumFromValue(status);
	}

	public void setStatusEnum(RunStatus runStatus) {
		this.status = runStatus.getValue();
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public LocalDateTime getNotificationSentAt() {
		return notificationSentAt;
	}

	public void setNotificationSentAt(LocalDateTime notificationSentAt) {
		this.notificationSentAt = notificationSentAt;
	}

	public LocalDateTime getExportedAt() {
		return exportedAt;
	}

	public void setExportedAt(LocalDateTime exportedAt) {
		this.exportedAt = exportedAt;
	}

	public long getErrorId() {
		return errorId;
	}

	public void setErrorId(long errorId) {
		this.errorId = errorId;
	}
}
