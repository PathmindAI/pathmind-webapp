package io.skymind.pathmind.services.training.cloud.aws;

import com.amazonaws.util.IOUtils;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.db.dao.ExecutionProviderMetaDataDAO;
import io.skymind.pathmind.services.training.ExecutionProvider;
import io.skymind.pathmind.services.training.JobSpec;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class AWSExecutionProvider implements ExecutionProvider {
    private final AWSApiClient client;

    public AWSExecutionProvider(AWSApiClient client) {
        this.client = client;
    }

    @Override
    public String execute(JobSpec job) {
        throw new UnsupportedOperationException("Not currently supported");
    }

    @Override
    public String uploadModel(long modelId, byte[] modelFile) {
        File model = null;
        try {
            model = File.createTempFile("pathmind", UUID.randomUUID().toString());
            FileUtils.writeByteArrayToFile(model, modelFile);
            client.fileUpload("test-training-dynamic-files.pathmind.com", modelId + "/model.zip", model);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (model != null) {
                model.delete();
            }
            return null;
        }
    }

    @Override
    public void stop(String jobHandle) {
        throw new UnsupportedOperationException("Not currently supported");
    }

    @Override
    public RunStatus status(String jobHandle) {
        throw new UnsupportedOperationException("Not currently supported");
    }

    @Override
    public Map<String, String> progress(String jobHandle) {
        throw new UnsupportedOperationException("Not currently supported");
    }

    @Override
    public Map<String, String> progress(String jobHandle, RunStatus runStatus) {
        throw new UnsupportedOperationException("Not currently supported");
    }

    @Override
    public byte[] policy(String jobHandle, String trainingRun) {
        throw new UnsupportedOperationException("Not currently supported");
    }

    @Override
    public Map.Entry<@NotNull String, byte[]> snapshot(String jobHandle, String trainingRun) {
        throw new UnsupportedOperationException("Not currently supported");
    }

    @Override
    public String uploadCheckpoint(byte[] checkpointFile) {
        throw new UnsupportedOperationException("Not currently supported");
    }

    @Override
    public String console(String jobHandle) {
        throw new UnsupportedOperationException("Not currently supported");
    }

    @Override
    public ExecutionProviderMetaDataDAO.ExecutionProviderClass executionProviderClass() {
        return ExecutionProviderMetaDataDAO.ExecutionProviderClass.AWS;
    }
}
