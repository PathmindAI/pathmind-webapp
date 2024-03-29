package io.skymind.pathmind.shared.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.skymind.pathmind.shared.utils.CloneUtils;
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
public class Policy extends Data implements DeepCloneableInterface<Policy> {
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
    private List<String> metricDisplayValues = new ArrayList<>();

    // The first Integer is the Index of the Metric, the <Integer, Double> are the Iteration number and the Mean Value of the Metric
    private Map<Integer, Map<Integer, Double>> sparklinesData = new LinkedHashMap<>();

    // GUI helper attributes
    Map<Integer, List<Double>> metricsLinesData;

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
                .sparklinesData(CloneUtils.cloneMapIntegerMapIntegerDouble(sparklinesData))
                .metricDisplayValues(metricDisplayValues == null ? null : new ArrayList<>(metricDisplayValues))
                .build());
    }
}
