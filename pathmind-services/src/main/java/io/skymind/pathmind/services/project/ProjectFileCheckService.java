package io.skymind.pathmind.services.project;

import io.skymind.pathmind.services.project.rest.ModelAnalyzerApiClient;
import io.skymind.pathmind.services.project.rest.dto.HyperparametersDTO;
import io.skymind.pathmind.shared.constants.InvalidModelType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Slf4j
public class ProjectFileCheckService {

    private static final String INVALID_MODEL_ERROR_MESSAGE = "Model needs to be updated. You can take a look at <a target='_blank' href='%s'>this article</a> for upgrade instructions.";

    private final ExecutorService checkerExecutorService;
    private final ModelAnalyzerApiClient client;
    private final String convertModelsToSupportRewardVariablesURL;

    public ProjectFileCheckService(ExecutorService checkerExecutorService, ModelAnalyzerApiClient client, String convertModelsToSupportRewardVariablesURL) {
        this.checkerExecutorService = checkerExecutorService;
        this.client = client;
        this.convertModelsToSupportRewardVariablesURL = convertModelsToSupportRewardVariablesURL;
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
                        // TODO OnurI: Remove after actual model-analyzer integration
                        analysisResult.setRewardVariables(Arrays.asList("sample[0]", "sample[1]", "sample[2]"));
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
        if (analysisResult != null && analysisResult.isOldVersionFound()) {
            return Optional.of(getErrorMessage(InvalidModelType.OLD_REWARD_VARIABLES));
        }
        else if (analysisResult == null || analysisResult.getActions() == null || analysisResult.getObservations() == null
                || analysisResult.getRewardVariables() == null) {
            return Optional.of("Unable to analyze the model.");
        }
        else if (analysisResult.getActions() != null && Integer.parseInt(analysisResult.getActions()) == 0) {
            return Optional.of("Number of actions found to be zero.");
        }
        else if (analysisResult.getObservations() != null && Integer.parseInt(analysisResult.getObservations()) == 0) {
            return Optional.of("Number of observations found to be zero.");
        }
        else if (analysisResult.getRewardVariables().isEmpty()) {
            return Optional.of("Reward variables list is empty.");
        }
        return Optional.empty();
    }

    private void setHyperparams(FileCheckResult result, HyperparametersDTO params) {
    	AnylogicFileCheckResult fileCheckResult = AnylogicFileCheckResult.class.cast(result);
        fileCheckResult.setNumAction(Integer.parseInt(params.getActions()));
    	fileCheckResult.setNumObservation(Integer.parseInt(params.getObservations()));
    	fileCheckResult.setRewardVariableFunction(params.getRewardFunction());
    	fileCheckResult.setRewardVariables(params.getRewardVariables());
    }

    public String getErrorMessage(InvalidModelType invalidModelType) {
        String articleUrl = getArticleUrlForInvalidReason(invalidModelType);
        return String.format(INVALID_MODEL_ERROR_MESSAGE, articleUrl);
    }

    private String getArticleUrlForInvalidReason(InvalidModelType invalidModelType) {
        switch (invalidModelType) {
            case OLD_REWARD_VARIABLES :
                return convertModelsToSupportRewardVariablesURL;
            default :
                // Currently only invalid model reason is reward variables 
                return convertModelsToSupportRewardVariablesURL;
        }
    }
}
