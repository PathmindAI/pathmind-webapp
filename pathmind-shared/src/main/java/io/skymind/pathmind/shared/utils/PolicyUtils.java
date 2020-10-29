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
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

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

    public static Optional<Policy> selectBestPolicy(List<Policy> policies) {
        if(policies == null)
            return Optional.empty();
        return policies.stream()
            .filter(p -> p.getRun().getStatusEnum().equals(RunStatus.Completed) ? p.hasFile() : true)
            .filter(p -> PolicyUtils.getLastScore(p) != null && !Double.isNaN(PolicyUtils.getLastScore(p)))
            .max(Comparator.comparing(PolicyUtils::getLastScore).thenComparing(PolicyUtils::getLastIteration));
    }

    public static void updateSimulationMetricsData(Policy policy) {
        if (policy == null) return;
        List<Metrics> metricsList = policy.getMetrics();
        policy.getSparklinesData().clear();
        policy.getSimulationMetrics().clear();
        policy.getUncertainty().clear();

        if (metricsList != null && metricsList.size() > 0) {
            // (k:iteration, v:(k:index, v:averageMeanValue))
            Map<Integer, Map<Integer, Double>> iterAndMetrics = metricsList.stream()
                .collect(groupingBy(Metrics::getIteration, LinkedHashMap::new,
                    groupingBy(Metrics::getIndex, LinkedHashMap::new,
                        Collectors.averagingDouble(Metrics::getMean))));

            // The Simulation Metric value shown is the mean value of the metric in the last iteration
            // Below sets the mean value of the metrics at the latest iteration into the list `simulationMetrics`
            int lastIteration = Collections.max(iterAndMetrics.keySet());
            iterAndMetrics.get(lastIteration).entrySet().stream()
                .forEach(e -> policy.getSimulationMetrics().add(e.getKey(), e.getValue()));

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
            Map<Integer, List<Double>> uncertaintyMap = policy.getMetricsRaws().stream()
                .collect(groupingBy(MetricsRaw::getIndex,
                    mapping(MetricsRaw::getValue, Collectors.toList())
                    )
                );

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
