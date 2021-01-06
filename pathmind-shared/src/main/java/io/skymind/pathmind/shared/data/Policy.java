package io.skymind.pathmind.shared.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public class Policy extends Data {
    private static final long serialVersionUID = -2089053095112497536L;
    // GUI helper attributes
    Map<Integer, List<Double>> metricsLinesData;
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
    // The first Integer is the Index of the Metric, the <Integer, Double> are the Iteration number and the Mean Value of the Metric
    private Map<Integer, Map<Integer, Double>> sparklinesData = new LinkedHashMap<>();
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
}
