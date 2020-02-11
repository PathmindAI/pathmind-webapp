package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.bus.EventBus;
import io.skymind.pathmind.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.policy.RewardScore;
import io.skymind.pathmind.data.utils.PolicyUtils;
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
        Map<Long, List<RewardScore>> rewardScores = RewardScoreRepository.getRewardScoresForPolicies(ctx, PolicyUtils.convertToPolicyIds(policies));
        policies.stream().forEach(policy -> policy.setScores(rewardScores.get(policy.getId())));
        return policies;
    }

    @Transactional
    public long insertPolicy(Policy policy) {
        ctx.transaction(configuration ->
        {
            DSLContext transactionCtx = DSL.using(configuration);

            policy.setId(PolicyRepository.insertPolicy(transactionCtx, policy));
            RewardScoreRepository.insertRewardScores(transactionCtx, policy.getId(), policy.getScores());
        });

        // STEPH -> This should not be required since the GUI has the parent objects but until I have to the time it's an extra database call.
        // NOTE -> Although we insert the RewardScore the data model object RewardScore does not have a reference to PolicyId so we don't need
        // to do anything special to link the RewardScore to the policy, it's all done through the Policy data model object.
        Policy savedPolicy = PolicyRepository.getPolicy(ctx, policy.getId());
        EventBus.post(new PolicyUpdateBusEvent(savedPolicy));
        return savedPolicy.getId();
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