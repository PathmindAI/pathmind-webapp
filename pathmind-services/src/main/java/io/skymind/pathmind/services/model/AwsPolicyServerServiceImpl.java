package io.skymind.pathmind.services.model;

import java.util.List;

import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.services.PolicyServerFilesCreator;
import io.skymind.pathmind.services.PolicyServerService;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import io.skymind.pathmind.shared.data.Observation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class AwsPolicyServerServiceImpl implements PolicyServerService {

    private static final String POLICY_SERVER_FILES = "policy_server_file/";

    private static final String OUTPUTS_PATH = POLICY_SERVER_FILES + "ouput/";

    private static final String SCHEMAS_PATH = POLICY_SERVER_FILES + "schema/";

    private final PolicyServerFilesCreator filesCreator;

    private final ObservationDAO observationDAO;

    private final AWSApiClient awsApiClient;

    @Autowired
    public AwsPolicyServerServiceImpl(
            PolicyServerFilesCreator filesCreator, ObservationDAO observationDAO, AWSApiClient awsApiClient) {
        this.filesCreator = filesCreator;
        this.observationDAO = observationDAO;
        this.awsApiClient = awsApiClient;
    }

    @Override
    public String createSchemaYaml(long modelId) {
        List<Observation> observationsForModel = observationDAO.getObservationsForModel(modelId);
        return filesCreator.createSchemaYaml(observationsForModel);
    }

    @Override
    public void saveSchemaYamlFile(long modelId, byte[] schemaYaml) {
        awsApiClient.fileUpload(SCHEMAS_PATH + modelId, schemaYaml);
    }

    @Override
    public byte[] getSchemaYamlFile(long modelId) {
        return awsApiClient.fileContents(SCHEMAS_PATH + modelId, true);
    }
}
