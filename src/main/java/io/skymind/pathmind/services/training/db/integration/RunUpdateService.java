package io.skymind.pathmind.services.training.db.integration;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.services.training.progress.Progress;
import io.skymind.pathmind.services.training.progress.RewardScore;

import java.util.List;

public interface RunUpdateService {
    List<Long> getExecutingRuns();
    void updateRun(long runId, RunStatus status, List<Progress> progresses);
    void savePolicyFile(long runId, String externalId, byte[] policyFile);
    void saveCheckpointFile(long runId, String externalId, byte[] checkpointFile);
    List<Policy> getStoppedPolicies(List<Long> runId);
    void cleanUpTemporary(long rundId);
    List<RewardScore> getScores(long runId, String policyExtId);
}
