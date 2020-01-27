package io.skymind.pathmind.ui.components.status;

import io.skymind.pathmind.services.project.FileCheckResult;

public interface StatusUpdater
{
    /**
     * Update state of progress bar
     * @param percentage value between 0 and 1
     */
    void updateStatus(double percentage);

    void updateError(String error);
    void fileSuccessfullyVerified(FileCheckResult result);

}
