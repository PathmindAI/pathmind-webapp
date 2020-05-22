package io.skymind.pathmind.shared.data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class Experiment extends ArchivableData
{
	public static String DEFAULT_REWARD_FUNCTION = "reward = after[0] - before[0];";
    
	private long modelId;
	private String rewardFunction;
	private LocalDateTime dateCreated;
	private LocalDateTime lastActivityDate;
	private String userNotes;

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
}
