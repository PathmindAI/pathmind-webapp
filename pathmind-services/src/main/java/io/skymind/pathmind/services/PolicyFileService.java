package io.skymind.pathmind.services;

import io.skymind.pathmind.shared.data.Policy;

public interface PolicyFileService {
    boolean hasPolicyFile(long policyId);
    byte[] getSnapshotFile(long id);
    byte[] getPolicyFile(long policyId);
    void savePolicyFile(Long policyId, byte[] policyFile);
    void savePolicyFile(Long runId, String finishPolicyName, byte[] policyFile);
    void saveCheckpointFile(Long policyId, byte[] policyFile);
    void saveCheckpointFile(Long runId, String finishPolicyName, byte[] checkPointFile);
}
