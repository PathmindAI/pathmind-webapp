package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.db.utils.DataUtils;
import io.skymind.pathmind.shared.bus.EventBus;
import io.skymind.pathmind.db.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.db.data.Policy;
import io.skymind.pathmind.db.data.RewardScore;
import io.skymind.pathmind.db.utils.PolicyUtils;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

import static java.text.MessageFormat.format;
import static java.util.Objects.nonNull;

@Slf4j
@Repository
public class PolicyDAO {
    private final DSLContext ctx;

    public PolicyDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public Policy getPolicy(long policyId) {
        Policy policy = PolicyRepository.getPolicy(ctx, policyId);
        policy.setScores(RewardScoreRepository.getRewardScoresForPolicy(ctx, policyId));
        return policy;
    }

    public List<Policy> getPoliciesForExperiment(long experimentId) {
        List<Policy> policies = PolicyRepository.getPoliciesForExperiment(ctx, experimentId);
        Map<Long, List<RewardScore>> rewardScores = RewardScoreRepository.getRewardScoresForPolicies(ctx, DataUtils.convertToIds(policies));
        policies.stream().forEach(policy -> policy.setScores(rewardScores.get(policy.getId())));
        return policies;
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

    public Long getPolicyId(long runId, String externalId) {
        return PolicyRepository.getPolicyIdByRunIdAndExternalId(ctx, runId, externalId);
    }

    public void setHasFile(Long policyId, boolean value) {
        PolicyRepository.setHasFile(ctx, policyId, value);
    }

    public Long assurePolicyId(Long runId, String finishPolicyName) {
        Assert.notNull(runId, "runId should be provided");
        Assert.hasText(finishPolicyName, "finalPolicyName should be provided");
        Long policyId = getPolicyId(runId, finishPolicyName);
        Assert.state(
                nonNull(policyId),
                format("Can not find policyId for run {0} and finishName {1}", runId, finishPolicyName)
        );
        return policyId;
    }
}