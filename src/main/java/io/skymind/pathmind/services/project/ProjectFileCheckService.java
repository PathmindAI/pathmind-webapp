package io.skymind.pathmind.services.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.services.project.rest.ModelAnalyzerApiClient;
import io.skymind.pathmind.services.project.rest.dto.HyperparametersDTO;
import io.skymind.pathmind.ui.components.status.StatusUpdater;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
public class ProjectFileCheckService {
    @Autowired
    ExecutorService checkerExecutorService;

    @Autowired
    ModelAnalyzerApiClient client;

    @Autowired
    ObjectMapper objectMapper;

    /* Creating temporary folder, extracting the zip file , File checking and deleting temporary folder*/
    public void checkFile(StatusUpdater statusUpdater, byte[] data) {
        Runnable runnable = () -> {
            try {
                statusUpdater.updateStatus(0);
                File tempFile = File.createTempFile("pathmind", UUID.randomUUID().toString());

                try {
                    FileUtils.writeByteArrayToFile(tempFile, data);
                    AnylogicFileChecker anylogicfileChecker = new AnylogicFileChecker(objectMapper);
                    //File check result.
                    final AnylogicFileCheckResult result = (AnylogicFileCheckResult) anylogicfileChecker.performFileCheck(statusUpdater, tempFile);

                    if (result.isFileCheckComplete() && result.isFileCheckSuccessful()) {
                        if (result.getPathmindMeta() == null) {
                            setHyperparams(result, client.analyze(tempFile));
                        }
                        statusUpdater.fileSuccessfullyVerified(result);
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

    private void setHyperparams(FileCheckResult result, HyperparametersDTO params) {
        if (params != null) {
            if (params.getActions() != null) {
                ((AnylogicFileCheckResult) (result)).setNumAction(Integer.parseInt(params.getActions()));
            }

            if (params.getObservations() != null) {
                ((AnylogicFileCheckResult)(result)).setNumObservation(Integer.parseInt(params.getObservations()));
            }

            ((AnylogicFileCheckResult)(result)).setRewardVariableFunction(params.getRewardFunction());
        } else {
            log.info("Model Analyzer returns null for the given model");
        }
    }

}
