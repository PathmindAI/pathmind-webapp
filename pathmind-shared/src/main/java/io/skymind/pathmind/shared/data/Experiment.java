package io.skymind.pathmind.shared.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import io.skymind.pathmind.shared.constants.RunStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import static io.skymind.pathmind.shared.constants.RunStatus.Error;
import static io.skymind.pathmind.shared.constants.RunStatus.NotStarted;
import static io.skymind.pathmind.shared.constants.RunStatus.Running;
import static io.skymind.pathmind.shared.constants.RunStatus.Starting;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Experiment extends ArchivableData {
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
    private List<Observation> modelObservations;
    private List<Observation> selectedObservations;

    // Helper attributes for error handling to prevent extra processing with training
    private String trainingError;
    private boolean allowRestartTraining = false;
    private boolean trainingStoppedEarly = false;
    private String trainingStoppedEarlyMessage;

    // TODO -> STEPH -> Helper attributes that maybe should be part of the experiment and I'm not 100% decided on the final details.
    private List<RewardVariable> rewardVariables;
    private Policy bestPolicy;

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

    // TODO -> STEPH -> DELETE -> Confirm this can be deleted after testing.
//    public void updateRuns(List<Run> runs) {
//        runs.forEach(this::updateRun);
//    }
//
//    public void updateRun(Run run) {
//        if (runs == null) {
//            runs = new ArrayList<>();
//            runs.add(run);
//        } else {
//            IntStream.range(0, runs.size())
//                    .filter(index -> runs.get(index).getId() == run.getId())
//                    .findFirst()
//                    .ifPresentOrElse(
//                            index -> runs.set(index, run),
//                            () -> runs.add(run));
//        }
//        updateTrainingStatus();
//    }

    public boolean isGoalsReached() {
        return hasGoals && Objects.equals(goalsReached, totalGoals);
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

    public String getTrainingError() {
        return trainingError;
    }

    public void setTrainingError(String trainingError) {
        this.trainingError = trainingError;
    }

    public boolean isAllowRestartTraining() {
        return allowRestartTraining;
    }

    public void setAllowRestartTraining(boolean allowRestartTraining) {
        this.allowRestartTraining = allowRestartTraining;
    }

    public boolean isTrainingStoppedEarly() {
        return trainingStoppedEarly;
    }

    public void setTrainingStoppedEarly(boolean trainingStoppedEarly) {
        this.trainingStoppedEarly = trainingStoppedEarly;
    }

    public String getTrainingStoppedEarlyMessage() {
        return trainingStoppedEarlyMessage;
    }

    public void setTrainingStoppedEarlyMessage(String trainingStoppedEarlyMessage) {
        this.trainingStoppedEarlyMessage = trainingStoppedEarlyMessage;
    }

    public boolean isTrainingError() {
        return StringUtils.isNotEmpty(trainingError);
    }

    public List<RewardVariable> getRewardVariables() {
        return rewardVariables;
    }

    public void setRewardVariables(List<RewardVariable> rewardVariables) {
        this.rewardVariables = rewardVariables;
    }

    public Policy getBestPolicy() {
        return bestPolicy;
    }

    public void setBestPolicy(Policy bestPolicy) {
        this.bestPolicy = bestPolicy;
    }
}
