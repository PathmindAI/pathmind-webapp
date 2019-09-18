package io.skymind.pathmind.services.training;

import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.services.training.progress.Progress;

import java.util.List;

public interface RunUpdateService {
    public List<Long> getExecutingRuns();
    public void updateRun(long runId, RunStatus status, List<Progress> progresses);
}
