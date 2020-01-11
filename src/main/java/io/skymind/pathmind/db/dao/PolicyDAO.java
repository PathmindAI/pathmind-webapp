package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.bus.EventBus;
import io.skymind.pathmind.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.policy.RewardScore;
import io.skymind.pathmind.data.utils.PolicyUtils;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class PolicyDAO
{
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
        Map<Long, List<RewardScore>> rewardScores = RewardScoreRepository.getRewardScoresForPolicies(ctx, PolicyUtils.convertToPolicyIds(policies));
        policies.stream().forEach(policy -> policy.setScores(rewardScores.get(policy.getId())));
        return policies;
    }

    /**
     * To avoid multiple download policy file from rescale server,
     * we put the "saving" for temporary
     * policy dao will check if there's real policy file exist or not
     */
    public boolean hasPolicyFile(long policyId) {
        return PolicyRepository.hasPolicyFile(ctx, policyId);
    }

    public byte[] getPolicyFile(long policyId) {
        return PolicyRepository.getPolicyFile(ctx, policyId);
    }

    public long insertPolicy(Policy policy) {
        long policyId = PolicyRepository.insertPolicy(ctx, policy);
        // STEPH -> This should not be required since the GUI has the parent objects but until I have to the time it's an extra database call.
        Policy savedPolicy = PolicyRepository.getPolicy(ctx, policyId);
        savedPolicy.setScores(RewardScoreRepository.getRewardScoresForPolicy(ctx, savedPolicy.getId()));
        EventBus.post(new PolicyUpdateBusEvent(savedPolicy));
        return policyId;
    }

    public List<Policy> getActivePoliciesForUser(long userId) {
        return PolicyRepository.getActivePoliciesForUser(ctx, userId);
    }

    public byte[] getSnapshotFile(long policyId) {
        return PolicyRepository.getSnapshotFile(ctx, policyId);
    }

}