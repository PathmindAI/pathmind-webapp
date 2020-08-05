package io.skymind.pathmind.shared.data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class Policy extends Data
{
    private static final long serialVersionUID = -2089053095112497536L;
	private long runId;
	private String externalId;

    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;

    // REFACTOR -> Same as Progress which is not saved to the database and is parsed back and forth...
    private List<RewardScore> scores;

    private boolean hasFile;
    private String checkPointFileKey;

    // Helper GUI attributes not stored in the database
	private Project project;
	private Model model;
	private Experiment experiment;
	private Run run;
	private transient List<Metrics> metrics;

    public List<RewardScore> getScores() {
        return scores == null ? Collections.emptyList() : scores;
    }

    public boolean hasFile() {
        return hasFile;
    }

    public void setHasFile(boolean hasFile) {
        this.hasFile = hasFile;
    }
}
