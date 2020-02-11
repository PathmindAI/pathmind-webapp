package io.skymind.pathmind.services.model;

import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Slf4j
@Service
class AwsModelServiceImpl implements ModelService {

    public static final String MODEL_FILES = "model_file/";

    private final ModelDAO modelDAO;
    private final AWSApiClient awsApiClient;

    public AwsModelServiceImpl(ModelDAO modelDAO, AWSApiClient awsApiClient) {
        this.modelDAO = modelDAO;
        this.awsApiClient = awsApiClient;
    }

    @Override
    public long addModelToProject(Model model, long id, String userNotes) {
        Assert.notNull(model, "Model should be provided");
        long result = modelDAO.addModelToProject(model, id, userNotes);
        saveModelFile(model.getId(), model.getFile());
        return result;
    }

    @Override
    public Optional<Model> getModel(long modelId) {
        return modelDAO.getModel(modelId);
    }

    @Override
    public byte[] getModelFile(long id) {
        byte[] content = awsApiClient.fileContents(MODEL_FILES + id, true);
        if (ArrayUtils.isEmpty(content)) {
            log.warn("Model File was not found in S3, fallback lookup in DB");
            content = modelDAO.getModelFile(id);
        }
        return content;
    }

    @Override
    public void saveModelFile(long modelId,  byte[] file) {
        awsApiClient.fileUpload(MODEL_FILES + modelId, file);
    }

}
