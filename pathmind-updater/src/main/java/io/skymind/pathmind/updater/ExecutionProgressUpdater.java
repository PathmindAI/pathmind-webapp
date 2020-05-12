package io.skymind.pathmind.updater;

public interface ExecutionProgressUpdater {
    /**
     * Update the progress and status of all currently running jobs
     */
    void update();
}
