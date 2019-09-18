package io.skymind.pathmind.data;

import io.skymind.pathmind.constants.RunType;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Run implements Data
{
	private long id;
	private String name;
	private int runType;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private long experimentId;

	public Run() {
	}

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}
}
