package io.skymind.pathmind.services;

import io.skymind.pathmind.shared.data.Policy;

public interface PolicyFileService {
    boolean hasPolicyFile(long policyId);

    boolean hasFreezingPolicyFile(long runId);

    byte[] getSnapshotFile(long id);

    byte[] getPolicyFile(long policyId);

    String getPolicyFileLocation(long policyId);

    byte[] getFreezingOrPolicyFile(Policy policy);

    byte[] getFreezingPolicyFile(long runId);

    void savePolicyFile(Long policyId, byte[] policyFile);

    void savePolicyFile(Long runId, String finishPolicyName, byte[] policyFile);

    void saveFreezingPolicyFile(Long runId, byte[] policyFile);

    void saveCheckpointFile(Long policyId, byte[] policyFile);

    void saveCheckpointFile(Long runId, String finishPolicyName, byte[] checkPointFile);
}
