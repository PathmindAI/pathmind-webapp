package io.skymind.pathmind.shared.data;

import java.time.LocalDateTime;

import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.constants.RunType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Run extends Data {
    private static final long serialVersionUID = 2452255564251125071L;
    private int runType;
    private long experimentId;
    private int status;
    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;
    private LocalDateTime notificationSentAt;
    private LocalDateTime exportedAt;
    private LocalDateTime ec2CreatedAt;
    private long trainingErrorId;
    private String jobId;
    private String rllibError;
    private String successMessage;
    private String warningMessage;
    private int completingUpdatesAttempts;

    // Helper attributes
    private Experiment experiment;
    private Model model;
    private Project project;

    // TODO -> Convert to a JOOQ converter.
    public RunType getRunTypeEnum() {
        return RunType.getEnumFromValue(runType);
    }

    // TODO -> Convert to a JOOQ converter.
    public void setRunTypeEnum(RunType runTypeEnum) {
        this.runType = runTypeEnum.getValue();
    }

    public RunStatus getStatusEnum() {
        return RunStatus.getEnumFromValue(status);
    }

    public void setStatusEnum(RunStatus runStatus) {
        this.status = runStatus.getValue();
    }
}
