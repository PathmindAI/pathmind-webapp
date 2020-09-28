package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.data.user.DeepCloneableInterface;
import io.skymind.pathmind.shared.utils.CloneUtils;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

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
    private boolean goalsReached;

	// Helper GUI attributes not stored in the database
	private Project project;
	private Model model;
	private transient List<Policy> policies;
	private transient List<Run> runs;

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

    public void updateRun(Run run) {
        if(runs == null) {
            runs = new ArrayList<>();
            runs.add(run);
        } else {
            IntStream.range(0, runs.size())
                    .filter(index -> runs.get(index).getId() == run.getId())
                    .findFirst()
                    .ifPresent(index -> runs.set(index, run));
        }
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
}
