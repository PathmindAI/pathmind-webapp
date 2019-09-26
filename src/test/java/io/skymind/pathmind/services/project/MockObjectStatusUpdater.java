package io.skymind.pathmind.services.project;

import io.skymind.pathmind.ui.components.status.StatusUpdater;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MockObjectStatusUpdater implements StatusUpdater {
    private static final Logger log = LogManager.getLogger(MockObjectStatusUpdater.class);

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

    @Override
    public void fileCheckComplete(FileCheckResult anylogicFileCheckResult) {
        log.info("File check complete:");

    }
}
