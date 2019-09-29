package io.skymind.pathmind.services.training.db.integration;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.services.training.progress.Progress;

import java.util.List;

public interface RunUpdateService {
    public List<Long> getExecutingRuns();
    public void updateRun(long runId, RunStatus status, List<Progress> progresses);
    public void savePolicyFile(long runId, String externalId, byte[] policyFile);
}
