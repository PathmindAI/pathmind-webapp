package io.skymind.pathmind.ui.components.status;

import io.skymind.pathmind.services.project.FileCheckResult;

public interface StatusUpdater
{
    /**
     * Update state of progress bar
     * @param percentage value between 0 and 1
     */
    public void updateStatus(double percentage);

    public void updateError(String error);
    public void fileSuccessfullyVerified();
    public void fileCheckComplete(FileCheckResult anylogicFileCheckResult);

}
