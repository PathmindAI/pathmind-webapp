package io.skymind.pathmind.services.model;

import io.skymind.pathmind.db.dao.ActionDAO;
import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.services.PolicyServerFilesCreator;
import io.skymind.pathmind.services.PolicyServerService;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import io.skymind.pathmind.shared.data.Action;
import io.skymind.pathmind.shared.data.Observation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
class AwsPolicyServerServiceImpl implements PolicyServerService {

    private static final String POLICY_SERVER_FILES = "policy_server_file/";

    private static final String OUTPUTS_PATH = POLICY_SERVER_FILES + "ouput/";

    private static final String SCHEMAS_PATH = POLICY_SERVER_FILES + "schema/";

    private final PolicyServerFilesCreator filesCreator;

    private final ObservationDAO observationDAO;

    private final ActionDAO actionDAO;

    private final AWSApiClient awsApiClient;

    @Autowired
    public AwsPolicyServerServiceImpl(
            PolicyServerFilesCreator filesCreator, ObservationDAO observationDAO, ActionDAO actionDAO,
            AWSApiClient awsApiClient) {
        this.filesCreator = filesCreator;
        this.observationDAO = observationDAO;
        this.actionDAO = actionDAO;
        this.awsApiClient = awsApiClient;
    }

    @Override
    public String createOutputYaml(long modelId) {
        List<Action> actionsForModel = actionDAO.getActionsForModel(modelId);
        return filesCreator.createOutputYaml(actionsForModel);
    }

    @Override
    public void saveOutputYamlFile(long modelId, byte[] outputYaml) {
        awsApiClient.fileUpload(OUTPUTS_PATH + modelId, outputYaml);
    }

    @Override
    public byte[] getOutputYamlFile(long modelId) {
        return awsApiClient.fileContents(OUTPUTS_PATH + modelId, true);
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
