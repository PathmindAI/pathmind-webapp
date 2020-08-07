package io.skymind.pathmind.shared.data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class Experiment extends ArchivableData
{
    private static final long serialVersionUID = -5041305878245823921L;
	private long modelId;
	private String rewardFunction;
	private LocalDateTime dateCreated;
	private LocalDateTime lastActivityDate;
    private String userNotes;
    private boolean isFavorite;

	// Helper GUI attributes not stored in the database
	private Project project;
	private Model model;
	private transient List<Policy> policies;
	private transient List<Run> runs;

	// IMPORTANT -> This is resolves #893. I looked at ThreadLocal as well as adjusting how the code uses the policies but deemed this to offer the best tradeoffs.
	public void setPolicies(List<Policy> policies) {
		this.policies = new CopyOnWriteArrayList<>(policies);
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
}
