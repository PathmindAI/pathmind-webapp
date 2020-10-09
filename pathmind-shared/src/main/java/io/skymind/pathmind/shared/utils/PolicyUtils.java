package io.skymind.pathmind.shared.utils;

import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.constants.RunType;
import io.skymind.pathmind.shared.data.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static io.skymind.pathmind.shared.utils.PathmindStringUtils.removeInvalidChars;
import static io.skymind.pathmind.shared.utils.PathmindStringUtils.toCamelCase;

@Slf4j
public class PolicyUtils
{

    private PolicyUtils() {
    }

    public static RunStatus getRunStatus(Policy policy) {
        if (policy.getRun().getRunTypeEnum() == RunType.DiscoveryRun && policy.getRun().getStatusEnum() == RunStatus.Running) {
            return policy.getStoppedAt() == null ? RunStatus.Running : RunStatus.Completed;
        } else {
            return policy.getRun().getStatusEnum();
        }
    }

    public static LocalDateTime getRunCompletedTime(Policy policy)
    {
        if (RunStatus.Completed == getRunStatus(policy))
            return policy.getStoppedAt();
        return null;
    }

    public static Double getLastScore(Policy policy) {
        if(policy == null || policy.getScores() == null || policy.getScores().isEmpty())
            return null;
        return policy.getScores().get(policy.getScores().size() - 1).getMean();
    }
    
    public static Integer getLastIteration(Policy policy) {
    	if(policy == null || policy.getScores() == null || policy.getScores().isEmpty())
    		return null;
    	return policy.getScores().get(policy.getScores().size() - 1).getIteration();
    }

    public static final String getElapsedTime(Policy policy) {
        return DateAndTimeUtils.formatDurationTime(RunUtils.getElapsedTime(policy.getRun()));
    }

    public static String parsePolicyName(String name) {
        try {
            return name.split("_")[2];
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            return null;
        }
    }

    public static List<Number> getMeanScores(Policy policy) {
        return policy.getScores().stream()
            .map(rewardScore -> rewardScore.getMean())
            .collect(Collectors.toList());
    }

    public static void loadPolicyDataModel(Policy policy, long policyId, Run run) {
        policy.setId(policyId);
        policy.setRun(run);
        policy.setExperiment(run.getExperiment());
        policy.setModel(run.getModel());
        policy.setProject(run.getProject());
    }

    public static List<Long> convertToPolicyIds(List<Policy> policies) {
        return policies.stream().map(policy -> policy.getId()).collect(Collectors.toList());
    }

    public static String generatePolicyFileName(Policy policy) {
        if(!ObjectUtils.allNotNull(policy, policy.getProject(), policy.getModel(), policy.getExperiment())) {
            return "-";
        }
        return removeInvalidChars(String.format("%s-M%s-%s-E%s-Policy.zip", toCamelCase(policy.getProject().getName()), policy.getModel().getName(), policy.getModel().getPackageName(), policy.getExperiment().getName()));
    }

    public static Policy selectBestPolicy(List<Policy> policies) {
        return policies.stream()
            .filter(p -> p.getRun().getStatusEnum().equals(RunStatus.Completed) ? p.hasFile() : true)
            .filter(p -> PolicyUtils.getLastScore(p) != null && !Double.isNaN(PolicyUtils.getLastScore(p)))
            .max(Comparator.comparing(PolicyUtils::getLastScore).thenComparing(PolicyUtils::getLastIteration))
            .orElse(null);
    }

    public static void updateSimulationMetricsData(Policy policy) {
        if (policy == null) return;
        List<Metrics> metricsList = policy.getMetrics();
        policy.getSparklinesData().clear();
        policy.getSimulationMetrics().clear();
        policy.getUncertainty().clear();

        if (metricsList != null && metricsList.size() > 0) {
            // The Simulation Metric value shown is the mean value of the metric in the last iteration
            // Below sets the mean value of the metrics at the latest iteration into the list `simulationMetrics`
            Metrics lastMetrics = metricsList.get(metricsList.size() - 1);

            lastMetrics.getMetricsThisIter().stream()
                    .forEach(metricsThisIter -> policy.getSimulationMetrics().add(metricsThisIter.getMean()));

            // index, metrics list
            Map<Integer, Map<Integer, Double>> sparkLineMap = new LinkedHashMap<>();

            // Loop by iteration
            metricsList.stream().forEach(metrics ->
                    // Loop by Number of Metrics for this Model, 
                    // with their index, min, max, and mean as values provided
                    metrics.getMetricsThisIter().forEach(mIter -> {
                        int index = mIter.getIndex(); // this is the index of the metric

                        // since this is for chart view, we need to make sure the order of data
                        // we SHOULD use LinkedHashMap instead of HashMap
                        Map<Integer, Double> data = sparkLineMap.containsKey(index) ? sparkLineMap.get(index) : new LinkedHashMap<>();
                        // Put Iteration Number and Mean Value of metric into this LinkedHashMap
                        data.put(metrics.getIteration(), mIter.getMean());
                        sparkLineMap.put(index, data);
                    })
            );
            policy.setSparklinesData(sparkLineMap);
        }

        List<MetricsRaw> metricsRawList = policy.getMetricsRaws();
        if (metricsRawList != null && metricsRawList.size() > 0) {
            Collections.sort(metricsRawList, Comparator.comparingInt(MetricsRaw::getIteration));
            Map<Integer, List<Double>> uncertaintyMap = MetricsRawUtils.toIndexAndMetricRawData(metricsRawList);

            policy.setUncertainty(uncertaintyMap.values().stream()
                    .map(list -> PathmindNumberUtils.calculateUncertainty(list))
                    .collect(Collectors.toList()));
        }
    }
    
    public static boolean isGoalReached(RewardVariable rv, Policy policy) {
        Double metricValue = 0.0, uncertaintyValue = 0.0;
        if (policy.getUncertainty() != null && !policy.getUncertainty().isEmpty()) {
            // No data to calculate yet
            if (policy.getUncertainty().size() <= rv.getArrayIndex()) {
                return false;
            }
            String metricValueWithUncertainty = policy.getUncertainty().get(rv.getArrayIndex());
            Double[] metricAndUncertainity = parseMetricAndUncertainity(metricValueWithUncertainty);
            metricValue = metricAndUncertainity[0];
            // todo https://github.com/SkymindIO/pathmind-webapp/pull/2063#issuecomment-693542915
            // https://github.com/SkymindIO/pathmind-webapp/issues/2108
            // uncertaintyValue = metricAndUncertainity[1];
        } else {
            // No data to calculate yet
            if (policy.getSimulationMetrics().size() <= rv.getArrayIndex()) {
                return false;
            }
            metricValue = policy.getSimulationMetrics().get(rv.getArrayIndex());
        }
        if (rv.getGoalConditionTypeEnum() != null){
            if (rv.getGoalConditionTypeEnum().equals(GoalConditionType.GREATER_THAN_OR_EQUAL)) {
                return metricValue + uncertaintyValue >= rv.getGoalValue();
            } else {
                return metricValue - uncertaintyValue <= rv.getGoalValue();
            }
        }
        return false;
    }
    
    private static Double[] parseMetricAndUncertainity(String metricValueWithUncertainty) {
        String[] actualMetricBreakdown = metricValueWithUncertainty.split("\u2800\u00B1\u2800");
        return new Double[] {Double.parseDouble(actualMetricBreakdown[0]), Double.parseDouble(actualMetricBreakdown[1])};
    }
}
