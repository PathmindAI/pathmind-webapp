package io.skymind.pathmind.shared.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.utils.CloneUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Experiment extends ArchivableData  implements DeepCloneableInterface<Experiment> {

    public static final int REWARD_FUNCTION_MAX_LENGTH = 65535;

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
    private Policy bestPolicy;
    private transient List<Policy> policies;
    private transient List<Run> runs;
    private List<Observation> modelObservations;
    private List<Observation> selectedObservations;

    // Helper attributes for error handling to prevent extra processing with training
    private String trainingError;
    private boolean trainingStoppedEarly = false;
    private String trainingStoppedEarlyMessage;

    private List<RewardVariable> rewardVariables;
    private List<RewardVariable> selectedRewardVariables;

    public RunStatus getTrainingStatusEnum() {
        return RunStatus.getEnumFromValue(trainingStatus);
    }

    public void setTrainingStatusEnum(RunStatus trainingStatus) {
        this.trainingStatus = trainingStatus.getValue();
    }

    public boolean isTrainingCompleted() {
        return RunStatus.Completed.getValue() == trainingStatus;
    }

    public boolean isTrainingRunning() {
        return RunStatus.isRunning(getTrainingStatusEnum());
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
        if (runs == null) {
            runs = new ArrayList<>();
        }
        runs.add(run);
    }

    public boolean isGoalsReached() {
        return hasGoals && Objects.equals(goalsReached, totalGoals);
    }

    public boolean isTrainingError() {
        return StringUtils.isNotEmpty(trainingError);
    }

    public void addSelectedRewardVariable(RewardVariable rewardVariable) {
        if (selectedRewardVariables == null) {
            selectedRewardVariables = new ArrayList<>();
        }
        selectedRewardVariables.add(rewardVariable);
    }

    public void toggleSelectedVariable(RewardVariable rewardVariable) {
        if (selectedRewardVariables.contains(rewardVariable)) {
            selectedRewardVariables.remove(rewardVariable);
        } else {
            selectedRewardVariables.add(rewardVariable);
        }
    }
    @Override
    public Experiment shallowClone() {
        Experiment experiment = super.shallowClone(Experiment.builder()
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
                .sharedWithSupport(sharedWithSupport)
                .build());
        experiment.setArchived(isArchived());
        experiment.setName(getName());
        return experiment;
    }

    @Override
    public Experiment deepClone() {
        Experiment experiment = shallowClone();
        experiment.setProject(CloneUtils.shallowClone(project));
        experiment.setModel(CloneUtils.shallowClone(model));
        experiment.setPolicies(CloneUtils.shallowCloneList(policies));
        experiment.setRuns(CloneUtils.shallowCloneList(runs));
        experiment.setRewardVariables(CloneUtils.shallowCloneList(rewardVariables));
        return experiment;
    }
}
