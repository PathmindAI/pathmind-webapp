package io.skymind.pathmind.services.model;

import java.util.Optional;

import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import io.skymind.pathmind.shared.constants.ModelType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.utils.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Slf4j
class AwsModelServiceImpl implements ModelService {

    public static final String MODEL_FILES = "model_file/";
    private static final String MODEL_ALP_FILES = "model_alp_file/";

    private final ModelDAO modelDAO;
    private final AWSApiClient awsApiClient;

    public AwsModelServiceImpl(ModelDAO modelDAO, AWSApiClient awsApiClient) {
        this.modelDAO = modelDAO;
        this.awsApiClient = awsApiClient;
    }

    @Override
    public void addDraftModelToProject(Model model, long id, String userNotes) {
        Assert.notNull(model, "Model should be provided");
        model.setDraft(true);
        if (ModelType.isALModel(ModelType.fromValue(model.getModelType()))) {
            ModelUtils.extractAndSetPackageName(model);
        }
        modelDAO.addDraftModelToProject(model, id, userNotes);
        saveModelFile(model.getId(), model.getFile());
    }

    @Override
    public void updateDraftModel(Model model, String modelNotes) {
        Assert.notNull(model, "Model should be provided");
        Assert.isTrue(model.getId() != -1, "Model id should be provided");
        modelDAO.updateUserNotes(model.getId(), modelNotes);
    }

    @Override
    public Experiment resumeModelCreation(Model model, String modelNotes) {
        Assert.notNull(model, "Model should be provided");
        Assert.isTrue(model.getId() != -1, "Model id should be provided");
        return modelDAO.resumeModelCreation(model, modelNotes);
    }

    @Override
    public Optional<Model> getModel(long modelId) {
        return modelDAO.getModel(modelId);
    }

    public byte[] getModelFile(long modelId) {
        return awsApiClient.fileContents(buildModelPath(modelId), true);
    }

    public void saveModelFile(long modelId, byte[] file) {
        saveModelFile(modelId, 0, file);
    }

    public void saveModelFile(long modelId, long experimentId, byte[] file) {
        awsApiClient.fileUpload(buildModelPath(modelId, experimentId), file);
    }

    @Override
    public String buildModelPath(long modelId) {
        return buildModelPath(modelId, 0);
    }

    @Override
    public String buildModelPath(long modelId, long experimentId) {
        String path = MODEL_FILES + modelId;
        if (experimentId > 0) {
            path += "/"+ experimentId ;
        }
        return path;
    }

    @Override
    public String buildModelAlpPath(long modelId) {
        return MODEL_ALP_FILES + modelId;
    }

    @Override
    public void saveModelAlp(Model model) {
        assert model.getAlpFile() != null && model.getAlpFile().length > 0;
        awsApiClient.fileUpload(buildModelAlpPath(model.getId()), model.getAlpFile());
    }

    @Override
    public void removeModelAlp(Model model) {
        awsApiClient.fileDelete(buildModelAlpPath(model.getId()));
    }

    @Override
    public boolean hasModelAlp(long modelId) {
        return awsApiClient.fileExists(buildModelAlpPath(modelId));
    }

    @Override
    public Optional<byte[]> getModelAlp(long modelId) {
        return Optional.ofNullable(awsApiClient.fileContents(buildModelAlpPath(modelId), true));
    }
}
