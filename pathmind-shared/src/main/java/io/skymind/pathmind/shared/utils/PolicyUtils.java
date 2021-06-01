package io.skymind.pathmind.shared.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Metrics;
import io.skymind.pathmind.shared.data.MetricsRaw;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.Run;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import static io.skymind.pathmind.shared.utils.PathmindStringUtils.removeInvalidChars;
import static io.skymind.pathmind.shared.utils.PathmindStringUtils.toCamelCase;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

@Slf4j
public class PolicyUtils {

    private PolicyUtils() {
    }

    public static Double getLastScore(Policy policy) {
        if (policy == null || policy.getScores() == null || policy.getScores().isEmpty()) {
            return null;
        }
        return policy.getScores().get(policy.getScores().size() - 1).getMean();
    }

    public static Integer getLastIteration(Policy policy) {
        if (policy == null || policy.getScores() == null || policy.getScores().isEmpty()) {
            return null;
        }
        return policy.getScores().get(policy.getScores().size() - 1).getIteration();
    }

    public static void loadPolicyDataModel(Policy policy, long policyId, Run run) {
        policy.setId(policyId);
        policy.setRun(run);
        policy.setExperiment(run.getExperiment());
        policy.setModel(run.getModel());
        policy.setProject(run.getProject());
    }

    public static String generatePolicyFileName(Policy policy) {
        if (!ObjectUtils.allNotNull(policy, policy.getProject(), policy.getModel(), policy.getExperiment())) {
            return "-";
        }
        return removeInvalidChars(String.format("%s-M%s-%s-E%s-Policy.zip", toCamelCase(policy.getProject().getName()), policy.getModel().getName(), policy.getModel().getPackageName(), policy.getExperiment().getName()));
    }

    public static Optional<Policy> selectBestPolicy(Experiment experiment) {
        return selectBestPolicy(experiment.getPolicies());
    }

    public static Optional<Policy> selectBestPolicy(List<Policy> policies) {
        if (policies == null) {
            return Optional.empty();
        }
        return policies.stream()
                .filter(p -> p.getRun().getStatusEnum().equals(RunStatus.Completed) ? p.hasFile() : true)
                .filter(p -> {
                    Double lastScore = PolicyUtils.getLastScore(p);
                    return lastScore != null && !Double.isNaN(lastScore);
                })
                .max(Comparator.comparing(PolicyUtils::getLastScore).thenComparing(PolicyUtils::getLastIteration));
    }

    public static void updateSimulationMetricsData(Policy policy) {
        if (policy == null) {
            return;
        }
        List<Metrics> metricsList = policy.getMetrics();
        policy.getSparklinesData().clear();
        policy.getSimulationMetrics().clear();
        policy.getMetricDisplayValues().clear();

        if (metricsList != null && metricsList.size() > 0) {
            // (k:iteration, v:(k:index, v:averageMeanValue))
            Map<Integer, Map<Integer, Double>> iterAndMetrics = metricsList.stream()
                    .collect(groupingBy(Metrics::getIteration, LinkedHashMap::new,
                            groupingBy(Metrics::getIndex, LinkedHashMap::new,
                                    Collectors.averagingDouble(Metrics::getMean))));

            // The Simulation Metric value shown is the mean value of the metric in the last iteration
            // Below sets the mean value of the metrics at the latest iteration into the list `simulationMetrics`
            int lastIteration = Collections.max(iterAndMetrics.keySet());
            iterAndMetrics.get(lastIteration).forEach((key, value) -> policy.getSimulationMetrics().add(key, value));

            // (k:index, v:(k:iteration, v:averageMeanValue))
            Map<Integer, Map<Integer, Double>> sparkLineMap = metricsList.stream()
                    .collect(groupingBy(Metrics::getIndex, LinkedHashMap::new,
                            groupingBy(Metrics::getIteration, LinkedHashMap::new,
                                    Collectors.averagingDouble(Metrics::getMean))));

            policy.setSparklinesData(sparkLineMap);
        }

        List<MetricsRaw> metricsRawList = policy.getMetricsRaws();

        if (metricsRawList != null && metricsRawList.size() > 0) {
            // (k: index, v: meanValueList)
            Map<Integer, List<Double>> uncertaintyMap = metricsRawList.stream()
                    .collect(groupingBy(MetricsRaw::getIndex,
                            mapping(MetricsRaw::getValue, Collectors.toList())
                            )
                    );

            policy.setMetricDisplayValues(uncertaintyMap.values().stream()
                    .map(PathmindNumberUtils::calculateUncertainty)
                    .collect(Collectors.toList()));
        }
    }

    public static void updateCompareMetricsChartData(Policy bestPolicy) {

        // If empty then there is nothing to calculate.
        if(bestPolicy.getSparklinesData().isEmpty()) {
            return;
        }

        Map<Integer, List<Double>> allLinesData = new LinkedHashMap<>();
        // Get first metric's sparkline data
        Map<Integer, Double> firstMetricSparklineData = bestPolicy.getSparklinesData().get(0);
        // Get first metric's last iteration number
        // all sparklines should have the same number of iterations
        List<Integer> iterationList = new ArrayList<>(firstMetricSparklineData.keySet());
        int maxIteration = iterationList.get(firstMetricSparklineData.size() - 1);

        // save a list of sparkline datum per metric per iteration
        for (int i = 0; i <= maxIteration; i++) {
            final int iterationNumber = i;
            List<Double> thisIterationMetricValues = new ArrayList<>();
            bestPolicy.getSparklinesData().forEach((metricIndex, sparklineData) ->
                    thisIterationMetricValues.add(sparklineData.get(iterationNumber)));
            allLinesData.put(i, thisIterationMetricValues);
        }

        bestPolicy.setMetricsLinesData(allLinesData);
    }
}
