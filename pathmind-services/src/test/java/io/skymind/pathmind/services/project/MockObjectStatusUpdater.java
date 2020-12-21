package io.skymind.pathmind.services.project;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockObjectStatusUpdater implements StatusUpdater<Object> {

    @Override
    public void updateStatus(double percentage) {
        log.info("Update status");
    }

    @Override
    public void updateError(String error) {
        log.info("Update error");
    }

    @Override
    public void fileSuccessfullyVerified(Object result) {
        log.info("File successfully verified:");
    }
}
