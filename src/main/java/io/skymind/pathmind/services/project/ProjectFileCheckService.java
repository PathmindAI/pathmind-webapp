package io.skymind.pathmind.services.project;

import io.skymind.pathmind.ui.components.status.StatusUpdater;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProjectFileCheckService {
    private static final Logger log = LogManager.getLogger(ProjectFileCheckService.class);

    // TODO -> Remove the showError flag, it's only for testing.
    public static void checkFile(StatusUpdater statusUpdater, boolean isShowError) throws IOException {


        Properties props = new Properties();
        props.load(ProjectFileCheckService.class.getClassLoader().getResourceAsStream("application.properties"));
        int threadPoolSize = Integer.parseInt(props.getProperty("poolsize"));


        Runnable runnable = () -> {
            try {
                File file = new File("D:/pathmind/CoffeeShopAnylogic Exported.zip");
                AnylogicFileChecker anylogicfileChecker = new AnylogicFileChecker();
                anylogicfileChecker.performFileCheck(file);
                //Thread.sleep(300);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                statusUpdater.updateError("File check interrupted.");
            } finally {
                log.info("Checking : completed");
                statusUpdater.done();
            }
        };

        ExecutorService executor = checkerExecutorService(threadPoolSize);
        executor.submit(runnable);
    }

    private static ExecutorService checkerExecutorService(int threadPoolSize) {
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        return executor;
    }

}
