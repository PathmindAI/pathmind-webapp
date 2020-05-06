package io.skymind.pathmind.services.model;

import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.utils.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
class AwsModelServiceImpl implements ModelService {

    public static final String MODEL_FILES = "model_file/";

    private final ModelDAO modelDAO;
    private final RewardVariableDAO rewardVariableDAO;
    private final AWSApiClient awsApiClient;

    public AwsModelServiceImpl(ModelDAO modelDAO, RewardVariableDAO rewardVariableDAO,
            AWSApiClient awsApiClient) {
        this.modelDAO = modelDAO;
        this.rewardVariableDAO = rewardVariableDAO;
        this.awsApiClient = awsApiClient;
    }

    @Override
    public void addDraftModelToProject(Model model, long id, String userNotes) {
        Assert.notNull(model, "Model should be provided");
        model.setDraft(true);
        ModelUtils.extractAndSetPackageName(model);
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
    public void updateModelRewardVariables(Model model, List<RewardVariable> rewardVariableList) {
        Assert.notNull(model, "Model should be provided");
        rewardVariableDAO.deleteModelRewardVariables(model.getId());
        if (rewardVariableList != null) {
            rewardVariableList.forEach(rv -> rv.setModelId(model.getId()));
            rewardVariableDAO.saveRewardVariables(rewardVariableList);
        }
    }

    @Override
    public long resumeModelCreation(Model model, String modelNotes) {
        Assert.notNull(model, "Model should be provided");
        Assert.isTrue(model.getId() != -1, "Model id should be provided");
        return modelDAO.resumeModelCreation(model, modelNotes);
    }

    @Override
    public Optional<Model> getModel(long modelId) {
        return modelDAO.getModel(modelId);
    }

    @Override
    public List<RewardVariable> getModelRewardVariables(long modelId) {
        return rewardVariableDAO.getRewardVariablesForModel(modelId);
    }

    @Override
    public byte[] getModelFile(long modelId) {
        return awsApiClient.fileContents(buildModelPath(modelId), true);
    }

    public void saveModelFile(long modelId,  byte[] file) {
        awsApiClient.fileUpload(buildModelPath(modelId), file);
    }

    @Override
    public String buildModelPath(long modelId) {
        return MODEL_FILES + modelId;
    }
}
