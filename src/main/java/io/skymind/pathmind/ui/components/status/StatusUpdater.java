package io.skymind.pathmind.ui.components.status;

public interface StatusUpdater
{
    /**
     * Update state of progress bar
     * @param percentage value between 0 and 1
     */
    public void updateStatus(double percentage);

    public void updateError(String error);
    public void fileSuccessfullyVerified();

}
