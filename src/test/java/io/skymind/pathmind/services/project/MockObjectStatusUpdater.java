package io.skymind.pathmind.services.project;

import io.skymind.pathmind.ui.components.status.StatusUpdater;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockObjectStatusUpdater implements StatusUpdater {

    @Override
    public void updateStatus(double percentage) {
        log.info("Update status");
    }

    @Override
    public void updateError(String error) {
        log.info("Update error");
    }

    @Override
    public void fileSuccessfullyVerified() {
        log.info("File successfully verified:");
    }
}
