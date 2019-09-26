package io.skymind.pathmind.data;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;

import java.time.LocalDateTime;

public class Run extends Data
{
	private int runType;
	private LocalDateTime startedAt;
	private LocalDateTime stoppedAt;
	private long experimentId;
	private int status;

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
}
