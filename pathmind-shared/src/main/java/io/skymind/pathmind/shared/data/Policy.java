package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.data.user.DeepCloneableInterface;
import io.skymind.pathmind.shared.utils.CloneUtils;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Policy extends Data implements DeepCloneableInterface
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
	private transient List<MetricsRaw> metricsRaws;

    // Helper Simulation Metrics GUI attributes not stored in the database
    private List<Double> simulationMetrics = new ArrayList<>();
    private List<double[]> sparklinesData = new ArrayList<>();
    private List<String> uncertainty = new ArrayList<>();

    public List<RewardScore> getScores() {
        return scores == null ? Collections.emptyList() : scores;
    }

    public boolean hasFile() {
        return hasFile;
    }

    public void setHasFile(boolean hasFile) {
        this.hasFile = hasFile;
    }

    @Override
    public Policy shallowClone() {
        return super.shallowClone(Policy.builder()
                .runId(runId)
                .externalId(externalId)
                .startedAt(startedAt)
                .stoppedAt(stoppedAt)
                .scores(CloneUtils.shallowCloneList(scores))
                .hasFile(hasFile)
                .checkPointFileKey(checkPointFileKey)
                .project(CloneUtils.shallowClone(project))
                .model(CloneUtils.shallowClone(model))
                .experiment(CloneUtils.shallowClone(experiment))
                .run(CloneUtils.shallowClone(run))
                .metrics(CloneUtils.shallowCloneList(metrics))
                .metricsRaws(CloneUtils.shallowCloneList(metricsRaws))
                .simulationMetrics(simulationMetrics == null ? null : new ArrayList<>(simulationMetrics))
                .sparklinesData(CloneUtils.cloneListDoubleArrays(sparklinesData))
                .uncertainty(uncertainty == null ? null : new ArrayList<>(uncertainty))
                .build());
    }
}
