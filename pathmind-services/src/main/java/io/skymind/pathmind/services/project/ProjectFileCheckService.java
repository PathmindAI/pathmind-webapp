package io.skymind.pathmind.services.project;

import io.skymind.pathmind.services.project.rest.ModelAnalyzerApiClient;
import io.skymind.pathmind.services.project.rest.dto.AnalyzeRequestDTO;
import io.skymind.pathmind.services.project.rest.dto.HyperparametersDTO;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import io.skymind.pathmind.shared.constants.InvalidModelType;
import io.skymind.pathmind.shared.data.Model;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Deprecated
@Slf4j
public class ProjectFileCheckService {

    public static final String INVALID_MODEL_ERROR_MESSAGE_WO_INSTRUCTIONS = "Model or Pathmind Helper may need to be updated.";
    public static final String INVALID_MODEL_ERROR_MESSAGE_WITH_INSTRUCTIONS = INVALID_MODEL_ERROR_MESSAGE_WO_INSTRUCTIONS + " Please read <a target='_blank' href='%s'>this article</a> or contact Pathmind support.";

    private final ExecutorService checkerExecutorService;
    @Getter
    private final ModelAnalyzerApiClient client;
    private final AWSApiClient awsApiClient;

    @Getter
    private final String convertModelsToSupportLatestVersionURL;

    public ProjectFileCheckService(ExecutorService checkerExecutorService, ModelAnalyzerApiClient client, String convertModelsToSupportLatestVersionURL, AWSApiClient awsApiClient) {
        this.checkerExecutorService = checkerExecutorService;
        this.client = client;
        this.convertModelsToSupportLatestVersionURL = convertModelsToSupportLatestVersionURL;
        this.awsApiClient = awsApiClient;
    }

    /* Creating temporary folder, extracting the zip file , File checking and deleting temporary folder*/
    public Future<?> checkFile(StatusUpdater statusUpdater, Model model, AnalyzeRequestDTO.ModelType type) {
        Runnable runnable = () -> {
            boolean uploadSuccess = false;
            File tempFile = null;
            try {
                statusUpdater.updateStatus(0);
                tempFile = File.createTempFile("pathmind", UUID.randomUUID().toString());

                FileUtils.writeByteArrayToFile(tempFile, model.getFile());
                AnylogicFileChecker anylogicfileChecker = new AnylogicFileChecker();
                //File check result.
                final FileCheckResult<Hyperparams> result = anylogicfileChecker.performFileCheck(statusUpdater, tempFile);
                if (result.isFileCheckComplete() && result.isFileCheckSuccessful()) {
                    AnyLogicModelInfo modelInfo = ((AnylogicFileCheckResult)result).getPriorityModelInfo();
                    String mainAgentName = AnyLogicModelInfo.getNameFromClass(modelInfo.getMainAgentClass());
                    String expClassName = AnyLogicModelInfo.getNameFromClass(modelInfo.getExperimentClass());
                    String expTypeName = modelInfo.getExperimentType().toString();
                    String pmHelperName = result.getDefinedHelpers().get(0).split("##")[1];
                    String reqId = "project_" + model.getProjectId();

                    HyperparametersDTO analysisResult =
                        client.analyze(tempFile, type, reqId, mainAgentName, expClassName, expTypeName, pmHelperName);

                    Optional<String> optionalError = verifyAnalysisResult(analysisResult);
                    if (optionalError.isPresent()) {
                        statusUpdater.updateError(optionalError.get());
                    } else {
                        Hyperparams hyperparams = buildHyperparams(analysisResult);
                        result.setParams(hyperparams);
                        model.setPathmindHelper(pmHelperName);
                        model.setMainAgent(mainAgentName);
                        model.setExperimentClass(expClassName);
                        model.setExperimentType(expTypeName);
                        statusUpdater.fileSuccessfullyVerified(result);
                        uploadSuccess = true;
                    }
                } else {
                    if (!result.isHelperPresent()) {
                        statusUpdater.updateError("You need to add PathmindHelper in your model.");
                    } else if (!result.isHelperUnique()) {
                        statusUpdater.updateError("Only one PathmindHelper per model is currently supported.: " + result.getDefinedHelpers());
                    } else if (!result.isValidRLPlatform()) {
                        statusUpdater.updateError("Invalid model. Please use the exported model for Pathmind.");
                    } else {
                        statusUpdater.updateError("The uploaded file is invalid, check it and upload again.");
                    }
                }

            } catch (Exception e) {
                log.error("File check interrupted.", e);
                statusUpdater.updateError("File check interrupted.");
            } finally {
                if (!uploadSuccess) {
                    String s3Path = String.format("model_failed/%d_model_%d.zip", model.getProjectId(), System.currentTimeMillis());
                    awsApiClient.fileUpload(s3Path, tempFile);
                    log.info("failed model is uploaded to {}/{}", awsApiClient.getBucketName(), s3Path);
                }
                FileUtils.deleteQuietly(tempFile);
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

    private Hyperparams buildHyperparams(HyperparametersDTO params) {
    	return Hyperparams.builder()
                .numAction(Integer.parseInt(params.getActions()))
                .numObservation(Integer.parseInt(params.getObservations()))
                .rewardVariableFunction(params.getRewardFunction())
                .rewardVariableNames(params.getRewardVariableNames())
                .rewardVariableTypes(params.getRewardVariableTypes())
                .observationNames(params.getObservationNames())
                .observationTypes(params.getObservationTypes())
                .modelType(params.getMode())
                .actionMask(params.isActionMask())
                .simulationParams(params.getAgentParams())
                .numberOfAgents(Integer.parseInt(params.getAgents())).build();

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
                return convertModelsToSupportLatestVersionURL;
            default :
                // Currently only invalid model reason is reward variables 
                return convertModelsToSupportLatestVersionURL;
        }
    }
}
