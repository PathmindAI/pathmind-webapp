package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.user.DeepCloneableInterface;
import io.skymind.pathmind.shared.utils.CloneUtils;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static io.skymind.pathmind.shared.constants.RunStatus.Error;
import static io.skymind.pathmind.shared.constants.RunStatus.NotStarted;
import static io.skymind.pathmind.shared.constants.RunStatus.Running;
import static io.skymind.pathmind.shared.constants.RunStatus.Starting;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Experiment extends ArchivableData implements DeepCloneableInterface<Experiment>
{
    private static final long serialVersionUID = -5041305878245823921L;
	private long modelId;
	private String rewardFunction;
	private LocalDateTime dateCreated;
	private LocalDateTime lastActivityDate;
    private String userNotes;
    private boolean isFavorite;
    private boolean hasGoals;
    private int totalGoals;
    private int goalsReached;
    private int trainingStatus;
    private boolean sharedWithSupport;

	// Helper GUI attributes not stored in the database
	private Project project;
	private Model model;
	private transient List<Policy> policies;
	private transient List<Run> runs;

    public RunStatus getTrainingStatusEnum() {
        return RunStatus.getEnumFromValue(trainingStatus);
    }

    public void setTrainingStatusEnum(RunStatus trainingStatus) {
        this.trainingStatus = trainingStatus.getValue();
    }

	// IMPORTANT -> This is resolves #893. I looked at ThreadLocal as well as adjusting how the code uses the policies but deemed this to offer the best tradeoffs.
	public void setPolicies(List<Policy> policies) {
		this.policies = policies == null ? null : new CopyOnWriteArrayList<>(policies);
	}

    public boolean isDraft() {
        return getRuns() == null || getRuns().isEmpty();
    }

    public List<Run> getRuns() {
        return runs == null ? new ArrayList<>() : runs;
    }

    public void addRun(Run run) {
	    if(runs == null)
	        runs = new ArrayList<>();
	    runs.add(run);
    }

    public void updateRuns(List<Run> runs) {
        runs.forEach(this::updateRun);
    }
    public void updateRun(Run run) {
        if(runs == null) {
            runs = new ArrayList<>();
            runs.add(run);
        } else {
            IntStream.range(0, runs.size())
                    .filter(index -> runs.get(index).getId() == run.getId())
                    .findFirst()
                    .ifPresentOrElse(
                            index -> runs.set(index, run),
                            () -> runs.add(run));
        }
        updateTrainingStatus();
    }

    public boolean isGoalsReached() {
        return hasGoals && Objects.equals(goalsReached, totalGoals);
    }

    @Override
    public Experiment shallowClone() {
        return super.shallowClone(Experiment.builder()
                .modelId(modelId)
                .rewardFunction(rewardFunction)
                .dateCreated(dateCreated)
                .lastActivityDate(lastActivityDate)
                .userNotes(userNotes)
                .isFavorite(isFavorite)
                .trainingStatus(trainingStatus)
                .hasGoals(hasGoals)
                .goalsReached(goalsReached)
                .totalGoals(totalGoals)
                .build());
    }

    @Override
    public Experiment deepClone() {
	    Experiment experiment = shallowClone();
        experiment.setProject(CloneUtils.shallowClone(project));
        experiment.setModel(CloneUtils.shallowClone(model));
        experiment.setPolicies(CloneUtils.shallowCloneList(policies));
        experiment.setRuns(CloneUtils.shallowCloneList(runs));
        return experiment;
    }

    public void updateTrainingStatus() {
        RunStatus status = getRuns().stream()
                .map(Run::getStatusEnum)
                .min(Comparator.comparingInt(RunStatus::getValue))
                .orElse(NotStarted);

        // In Running status, there can be some runs completed while others are yet to be started
        // So checking that to make sure
        if (status == NotStarted || status == Starting) {
            if (getRuns().stream()
                    .map(Run::getStatusEnum)
                    .map(RunStatus::getValue)
                    .anyMatch(statusVal -> statusVal > Starting.getValue())) {
                status = Running;
            }
        }

        if (status == RunStatus.Killed) {
            if (getRuns().stream()
                    .map(Run::getTrainingErrorId)
                    .anyMatch(errorId -> errorId > 0)) {
                status = Error;
            }
        }
        setTrainingStatusEnum(status);

    }
}
