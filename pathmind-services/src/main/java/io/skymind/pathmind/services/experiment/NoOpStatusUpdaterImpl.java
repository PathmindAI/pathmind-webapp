package io.skymind.pathmind.services.experiment;

import io.skymind.pathmind.services.project.AnylogicFileCheckResult;
import io.skymind.pathmind.services.project.StatusUpdater;
import lombok.Getter;

@Getter
public class NoOpStatusUpdaterImpl implements StatusUpdater<AnylogicFileCheckResult> {

    private String error;
    private AnylogicFileCheckResult result;

    @Override
    public void updateStatus(double percentage) {
        // ~ no op
    }

    @Override
    public void updateError(String error) {
        this.error = error;
    }

    @Override
    public void fileSuccessfullyVerified(AnylogicFileCheckResult result) {
        this.result = result;
    }
}