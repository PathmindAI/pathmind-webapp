package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.constants.RunType;
import io.skymind.pathmind.shared.data.user.DeepCloneableInterface;
import io.skymind.pathmind.shared.utils.CloneUtils;
import lombok.*;

import java.awt.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Run extends Data implements DeepCloneableInterface<Run> {
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

    @Override
    public Run shallowClone() {
        return super.shallowClone(Run.builder()
                .runType(runType)
                .experimentId(experimentId)
                .status(status)
                .startedAt(startedAt)
                .stoppedAt(stoppedAt)
                .notificationSentAt(notificationSentAt)
                .exportedAt(exportedAt)
                .ec2CreatedAt(ec2CreatedAt)
                .trainingErrorId(trainingErrorId)
                .jobId(jobId)
                .rllibError(rllibError)
                .successMessage(successMessage)
                .warningMessage(warningMessage)
                .build());
    }

    @Override
    public Run deepClone() {
        Run run = shallowClone();
        run.setExperiment(CloneUtils.shallowClone(experiment));
        run.setModel(CloneUtils.shallowClone(model));
        run.setProject(CloneUtils.shallowClone(project));
        return run;
    }
}
