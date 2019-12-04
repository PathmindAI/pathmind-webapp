package io.skymind.pathmind.services.training.db.integration;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.Policy;

import java.util.List;

public interface RunUpdateService {
    List<Long> getExecutingRuns();
    void updateRun(long runId, RunStatus status, List<Policy> policies);
    void savePolicyFile(long runId, String externalId, byte[] policyFile);
    List<Policy> getStoppedPolicies(List<Long> runId);
    void cleanUpTemporary(long rundId);
}
