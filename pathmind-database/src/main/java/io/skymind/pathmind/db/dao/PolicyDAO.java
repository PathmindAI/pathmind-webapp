package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.db.utils.DataUtils;
import io.skymind.pathmind.shared.data.Metrics;
import io.skymind.pathmind.shared.data.MetricsRaw;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardScore;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.text.MessageFormat.format;
import static java.util.Objects.nonNull;

@Slf4j
@Repository
public class PolicyDAO {
    private final DSLContext ctx;

    public PolicyDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public Optional<Policy> getPolicyIfAllowed(long policyId, long userId) {
        Optional<Policy> optionalPolicy  = PolicyRepository.getPolicyIfAllowed(ctx, policyId, userId);
        optionalPolicy
                .ifPresent(policy -> policy.setScores(RewardScoreRepository.getRewardScoresForPolicy(ctx, policyId)));
        return optionalPolicy;
    }

    public List<Policy> getPoliciesForExperiment(long experimentId) {
        List<Policy> policies = PolicyRepository.getPoliciesForExperiment(ctx, experimentId);
        Map<Long, List<RewardScore>> rewardScores = RewardScoreRepository.getRewardScoresForPolicies(ctx, DataUtils.convertToIds(policies));
        policies.stream().forEach(policy -> policy.setScores(rewardScores.get(policy.getId())));
        Map<Long, List<Metrics>> metricsMap = getMetricsForPolicies(DataUtils.convertToIds(policies));
        policies.stream().forEach(policy -> policy.setMetrics(metricsMap.get(policy.getId())));
        Map<Long, List<MetricsRaw>> metricsRawMap = MetricsRawRepository.getMetricsRawForPolicies(ctx, DataUtils.convertToIds(policies));
        policies.stream().forEach(policy -> policy.setMetricsRaws(metricsRawMap.get(policy.getId())));

        return policies;
    }

    public Map<Long, List<Metrics>> getMetricsForPolicies(List<Long> policyIds) {
        return MetricsRepository.getMetricsForPolicies(ctx, policyIds);
    }

    public Map<Long, List<MetricsRaw>> getMetricsRawForPolicies(List<Long> policyIds) {
        return MetricsRawRepository.getMetricsRawForPolicies(ctx, policyIds);
    }

    public Map<Long, Integer> getRewardScoresCountForExperiments(List<Long> experimentIds) {
        return RewardScoreRepository.getRewardScoresCountForExperiments(ctx, experimentIds);
    }

    public List<Policy> getActivePoliciesForUser(long userId) {
        return PolicyRepository.getActivePoliciesForUser(ctx, userId);
    }

    public void updateExportedDate(long policyId) {
        PolicyRepository.updateExportedDate(ctx, policyId);
    }

    public Long assurePolicyId(Long runId, String finishPolicyName) {
        Assert.notNull(runId, "runId should be provided");
        Assert.hasText(finishPolicyName, "finalPolicyName should be provided");
        Long policyId = PolicyRepository.getPolicyIdByRunIdAndExternalId(ctx, runId, finishPolicyName);
        Assert.state(
                nonNull(policyId),
                format("Can not find policyId for run {0} and finishName {1}", runId, finishPolicyName)
        );
        return policyId;
    }
}