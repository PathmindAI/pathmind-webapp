package io.skymind.pathmind.services.project;

import io.skymind.pathmind.ui.components.status.StatusUpdater;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

@Service
public class ProjectFileCheckService {
    private static final Logger log = LogManager.getLogger(ProjectFileCheckService.class);

    @Autowired
    ExecutorService checkerExecutorService;

    public void checkFile(StatusUpdater statusUpdater) throws IOException {

        Runnable runnable = () -> {
            try {
                File file = new File("D:/pathmind/CoffeeShopAnylogic Exported.zip");
                AnylogicFileChecker anylogicfileChecker = new AnylogicFileChecker();
                //Result set here.
                statusUpdater.fileCheckComplete(anylogicfileChecker.performFileCheck(file));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                statusUpdater.updateError("File check interrupted.");
            } finally {
                log.info("Checking : completed");
                statusUpdater.done();
            }
        };
        checkerExecutorService.submit(runnable);
    }

}
