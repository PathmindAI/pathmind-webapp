package io.skymind.pathmind.services.project;

import io.skymind.pathmind.ui.components.status.StatusUpdater;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Service
public class ProjectFileCheckService {
    private static final Logger log = LogManager.getLogger(ProjectFileCheckService.class);

    @Autowired
    ExecutorService checkerExecutorService;

    public void checkFile(StatusUpdater statusUpdater, byte[] data) {
        Runnable runnable = () -> {
            try {
                statusUpdater.updateStatus(0);
                File tempFile = File.createTempFile("pathmind", UUID.randomUUID().toString());
                try {
                    FileUtils.writeByteArrayToFile(tempFile, data);

                    AnylogicFileChecker anylogicfileChecker = new AnylogicFileChecker();

                    //Result set here.
                    final FileCheckResult result = anylogicfileChecker.performFileCheck(statusUpdater, tempFile);
                    if(result.isFileCheckComplete() && result.isFileCheckSuccessful()){
                        statusUpdater.fileSuccessfullyVerified();
                    } else {
                        log.error("File is not valid");
                        statusUpdater.updateError("File is not valid.");
                    }
                } finally {
                    tempFile.delete();
                }
            } catch (Exception e) {
                log.error("File check interrupted.", e);
                statusUpdater.updateError("File check interrupted.");
            } finally {
                log.info("Checking : completed");
            }
        };
        checkerExecutorService.submit(runnable);
    }

}
