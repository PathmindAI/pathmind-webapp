package io.skymind.pathmind.db.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.skymind.pathmind.shared.data.Metrics;
import io.skymind.pathmind.shared.data.MetricsRaw;
import io.skymind.pathmind.shared.data.Policy;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

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
        return PolicyRepository.getPolicyIfAllowed(ctx, policyId, userId);
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