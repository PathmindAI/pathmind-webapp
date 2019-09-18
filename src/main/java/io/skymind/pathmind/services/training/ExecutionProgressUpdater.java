package io.skymind.pathmind.services.training;

public interface ExecutionProgressUpdater {
    /**
     * Update the progress and status of all currently running jobs
     */
    public void update();
}
