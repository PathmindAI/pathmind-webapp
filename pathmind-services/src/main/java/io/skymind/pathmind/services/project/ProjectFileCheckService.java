package io.skymind.pathmind.services.project;

import io.skymind.pathmind.services.project.rest.ModelAnalyzerApiClient;
import io.skymind.pathmind.services.project.rest.dto.HyperparametersDTO;
import io.skymind.pathmind.shared.constants.InvalidModelType;
import io.skymind.pathmind.shared.data.Model;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Slf4j
public class ProjectFileCheckService {

    private static final String INVALID_MODEL_ERROR_MESSAGE_WITH_INSTRUCTIONS = "Model needs to be updated. You can take a look at <a target='_blank' href='%s'>this article</a> for upgrade instructions.";
    private static final String INVALID_MODEL_ERROR_MESSAGE_WO_INSTRUCTIONS = "Model needs to be uploaded again.";

    private final ExecutorService checkerExecutorService;
    private final ModelAnalyzerApiClient client;
    private final String convertModelsToSupportLastestVersionURL;

    public ProjectFileCheckService(ExecutorService checkerExecutorService, ModelAnalyzerApiClient client, String convertModelsToSupportLastestVersionURL) {
        this.checkerExecutorService = checkerExecutorService;
        this.client = client;
        this.convertModelsToSupportLastestVersionURL = convertModelsToSupportLastestVersionURL;
    }

    /* Creating temporary folder, extracting the zip file , File checking and deleting temporary folder*/
    public Future<?> checkFile(StatusUpdater statusUpdater, Model model) {
        Runnable runnable = () -> {
            try {
                statusUpdater.updateStatus(0);
                File tempFile = File.createTempFile("pathmind", UUID.randomUUID().toString());

                try {
                    FileUtils.writeByteArrayToFile(tempFile, model.getFile());
                    AnylogicFileChecker anylogicfileChecker = new AnylogicFileChecker();
                    //File check result.
                    final FileCheckResult result = anylogicfileChecker.performFileCheck(statusUpdater, tempFile);

                    if (result.isFileCheckComplete() && result.isFileCheckSuccessful()) {
                        HyperparametersDTO analysisResult = client.analyze(tempFile, "project_" + model.getProjectId());
                        Optional<String> optionalError = verifyAnalysisResult(analysisResult);
                        if (optionalError.isPresent()) {
                            statusUpdater.updateError(optionalError.get());
                        } else {
                            setHyperparams(result, analysisResult);
                            statusUpdater.fileSuccessfullyVerified(result);
                        }
                    } else {
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
        return checkerExecutorService.submit(runnable);
    }

    private Optional<String> verifyAnalysisResult(HyperparametersDTO param) {
        if (param != null && param.isOldVersionFound()) {
            return Optional.of(getErrorMessage(InvalidModelType.OLD_REWARD_VARIABLES));
        } else if (param == null || param.getObservationNames() == null
            || param.getObservationTypes() == null || param.getRewardVariableNames() == null || param.getRewardVariableTypes() == null) {
            return Optional.of("Unable to analyze the model.");
        } else if (param.getRewardVariableNames().isEmpty() || param.getRewardVariableTypes().isEmpty()) {
            return Optional.of("Failed to read reward variables.");
        } else if (param.getRewardVariableNames().size() != param.getRewardVariableTypes().size()) {
            return Optional.of("Should be the same number of reward variable names and types.");
        } else if (param.getObservationNames().isEmpty() || param.getObservationTypes().isEmpty()) {
            return Optional.of("Failed to read observations.");
        } else if (param.getObservationNames().size() != param.getObservationTypes().size()) {
            return Optional.of("Should be the same number of observation names and types.");
        } else if (param.getMode().isEmpty()) {
            return Optional.of("Failed to read model type.");
        } else if (!param.isEnabled()) {
            return Optional.of("Should enable PathmindHelper.");
        } else if (param.getAgents().isEmpty()) {
            return Optional.of("Failed to read the number of agents.");
        }
        return Optional.empty();
    }

    private void setHyperparams(FileCheckResult result, HyperparametersDTO params) {
    	AnylogicFileCheckResult fileCheckResult = AnylogicFileCheckResult.class.cast(result);
    	fileCheckResult.setNumObservation(Integer.parseInt(params.getObservations()));
    	fileCheckResult.setRewardVariableFunction(params.getRewardFunction());
    	fileCheckResult.setRewardVariableNames(params.getRewardVariableNames());
    	fileCheckResult.setRewardVariableTypes(params.getRewardVariableTypes());
    	fileCheckResult.setObservationNames(params.getObservationNames());
    	fileCheckResult.setObservationTypes(params.getObservationTypes());
    	fileCheckResult.setModelType(params.getMode());
    	fileCheckResult.setNumberOfAgents(Integer.parseInt(params.getAgents()));
    }

    public String getErrorMessage(InvalidModelType invalidModelType) {
        switch (invalidModelType) {
            case MISSING_OBSERVATIONS :
                return INVALID_MODEL_ERROR_MESSAGE_WO_INSTRUCTIONS;
            default:
                String articleUrl = getArticleUrlForInvalidReason(invalidModelType);
                return String.format(INVALID_MODEL_ERROR_MESSAGE_WITH_INSTRUCTIONS, articleUrl);
        }
    }

    private String getArticleUrlForInvalidReason(InvalidModelType invalidModelType) {
        switch (invalidModelType) {
            case OLD_REWARD_VARIABLES :
                return convertModelsToSupportLastestVersionURL;
            default :
                // Currently only invalid model reason is reward variables 
                return convertModelsToSupportLastestVersionURL;
        }
    }
}
