package io.skymind.pathmind.shared.utils;

import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.constants.RunType;
import io.skymind.pathmind.shared.data.Metrics;
import io.skymind.pathmind.shared.data.MetricsRaw;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.Run;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static io.skymind.pathmind.shared.utils.PathmindStringUtils.toCamelCase;
import static io.skymind.pathmind.shared.utils.PathmindStringUtils.removeInvalidChars;

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
                .filter(p -> PolicyUtils.getLastScore(p) != null && !Double.isNaN(PolicyUtils.getLastScore(p)))
                .max(Comparator.comparing(PolicyUtils::getLastScore).thenComparing(PolicyUtils::getLastIteration))
                .orElse(null);
    }

    public static void updateSimulationMetricsData(Policy policy) {
        List<Metrics> metricsList = policy == null ? null : policy.getMetrics();
        policy.getSparklinesData().clear();
        policy.getSimulationMetrics().clear();
        policy.getUncertainty().clear();

        if (metricsList != null && metricsList.size() > 0) {
            // set the last metrics
            Metrics lastMetrics = metricsList.get(metricsList.size() - 1);
            lastMetrics.getMetricsThisIter().stream()
                    .forEach(metricsThisIter -> policy.getSimulationMetrics().add(metricsThisIter.getMean()));

            // index, metrics list
            Map<Integer, List<Double>> sparkLineMap = new HashMap<>();
            metricsList.stream().forEach(metrics ->
                    metrics.getMetricsThisIter().forEach(mIter -> {
                        int index = mIter.getIndex();

                        List<Double> data = sparkLineMap.containsKey(index) ? sparkLineMap.get(index) : new ArrayList<>();
                        data.add(mIter.getMean());
                        sparkLineMap.put(index, data);
                    })
            );

            // convert List<Double> to double[] because sparLine needs an array of primitive types
            sparkLineMap.entrySet().stream()
                    .map(e -> e.getValue().stream().mapToDouble(Double::doubleValue).toArray())
                    .forEach(arr -> policy.getSparklinesData().add(arr));
        }

        List<MetricsRaw> metricsRawList = policy == null ? null : policy.getMetricsRaws();
        if (metricsRawList != null && metricsRawList.size() > 0) {
            Collections.sort(metricsRawList, Comparator.comparingInt(MetricsRaw::getIteration));
            Map<Integer, List<Double>> uncertaintyMap = MetricsRawUtils.toIndexAndMetricRawData(metricsRawList);

            policy.setUncertainty(uncertaintyMap.values().stream()
                    .map(list -> PathmindNumberUtils.calculateUncertainty(list))
                    .collect(Collectors.toList()));
        }
    }
}
