package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.bus.EventBus;
import io.skymind.pathmind.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.data.Policy;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PolicyDAO
{
    private final DSLContext ctx;

    public PolicyDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public Policy getPolicy(long policyId) {
          return PolicyRepository.getPolicy(ctx, policyId);
    }

    public List<Policy> getPoliciesForExperiment(long experimentId) {
        return PolicyRepository.getPoliciesForExperiment(ctx, experimentId);
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
        EventBus.post(new PolicyUpdateBusEvent(savedPolicy));
        return policyId;
    }

    public List<Policy> getActivePoliciesForUser(long userId) {
        return PolicyRepository.getActivePoliciesForUser(ctx, userId);
    }

    public void updatePolicyNameAndExternalId(long runId, String newExternalId, String oldExternalId) {
        PolicyRepository.updatePolicyNameAndExternalId(ctx, runId, newExternalId, oldExternalId);
    }
}