package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.constants.RunType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Run extends Data
{
	private int runType;
	private long experimentId;
	private int status;
	private LocalDateTime startedAt;
	private LocalDateTime stoppedAt;
	private LocalDateTime notificationSentAt;
	private LocalDateTime exportedAt;
	private long trainingErrorId;
	private String jobId;
    private String rLibError;

	// Helper attributes
	private Experiment experiment;
	private Model model;
	private Project project;

	// TODO -> Convert to a JOOQ converter.
	public void setRunTypeEnum(RunType runTypeEnum) {
		this.runType = runTypeEnum.getValue();
	}

	// TODO -> Convert to a JOOQ converter.
	public RunType getRunTypeEnum() {
		return RunType.getEnumFromValue(runType);
	}

	public RunStatus getStatusEnum() {
		return RunStatus.getEnumFromValue(status);
	}

	public void setStatusEnum(RunStatus runStatus) {
		this.status = runStatus.getValue();
	}

}
