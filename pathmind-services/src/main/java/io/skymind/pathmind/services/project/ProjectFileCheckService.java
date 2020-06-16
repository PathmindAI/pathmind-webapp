package io.skymind.pathmind.services.project;

import io.skymind.pathmind.services.project.rest.ModelAnalyzerApiClient;
import io.skymind.pathmind.services.project.rest.dto.HyperparametersDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Slf4j
public class ProjectFileCheckService {

    private final ExecutorService checkerExecutorService;
    private final ModelAnalyzerApiClient client;

    public ProjectFileCheckService(ExecutorService checkerExecutorService, ModelAnalyzerApiClient client) {
        this.checkerExecutorService = checkerExecutorService;
        this.client = client;
    }

    /* Creating temporary folder, extracting the zip file , File checking and deleting temporary folder*/
    public void checkFile(StatusUpdater statusUpdater, byte[] data) {
        Runnable runnable = () -> {
            try {
                statusUpdater.updateStatus(0);
                File tempFile = File.createTempFile("pathmind", UUID.randomUUID().toString());

                try {
                    FileUtils.writeByteArrayToFile(tempFile, data);
                    AnylogicFileChecker anylogicfileChecker = new AnylogicFileChecker();
                    //File check result.
                    final FileCheckResult result = anylogicfileChecker.performFileCheck(statusUpdater, tempFile);

                    if (result.isFileCheckComplete() && result.isFileCheckSuccessful()) {
                        HyperparametersDTO analysisResult = client.analyze(tempFile);
                        Optional<String> optionalError = verifyAnalysisResult(analysisResult);
                        if (optionalError.isPresent()) {
                            statusUpdater.updateError(optionalError.get());
                        }
                        else {
                            setHyperparams(result, analysisResult);
                            statusUpdater.fileSuccessfullyVerified(result);
                        }
                    }
                    else {
                        statusUpdater.updateError("The uploaded file is invalid, check it and upload again.");
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

    private Optional<String> verifyAnalysisResult(HyperparametersDTO analysisResult) {
        if (analysisResult == null || analysisResult.getActions() == null || analysisResult.getObservations() == null 
                || analysisResult.getRewardVariablesCount() == null || analysisResult.getActionTupleSize() == null) {
            return Optional.of("Unable to analyze the model.");
        }
        else if (analysisResult.getActions() != null && Integer.parseInt(analysisResult.getActions()) == 0) {
            return Optional.of("Number of actions found to be zero.");
        }
        else if (analysisResult.getObservations() != null && Integer.parseInt(analysisResult.getObservations()) == 0) {
            return Optional.of("Number of observations found to be zero.");
        }
        return Optional.empty();
    }

    private void setHyperparams(FileCheckResult result, HyperparametersDTO params) {
    	AnylogicFileCheckResult fileCheckResult = AnylogicFileCheckResult.class.cast(result);
        fileCheckResult.setNumAction(Integer.parseInt(params.getActions()));
    	fileCheckResult.setNumObservation(Integer.parseInt(params.getObservations()));
    	fileCheckResult.setRewardVariablesCount(Integer.parseInt(params.getRewardVariablesCount()));
    	fileCheckResult.setRewardVariableFunction(params.getRewardFunction());
    	fileCheckResult.setActionTupleSize(Integer.parseInt(params.getActionTupleSize()));
    }

}
