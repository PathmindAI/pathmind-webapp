package io.skymind.pathmind.services.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.services.PolicyServerFilesCreator;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import io.skymind.pathmind.services.training.cloud.aws.api.dto.DeploymentMessage;
import io.skymind.pathmind.shared.constants.ModelType;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.services.PolicyServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
class AwsPolicyServerServiceImpl implements PolicyServerService {

    private final PolicyServerFilesCreator filesCreator;

    private final ObservationDAO observationDAO;

    private final RunDAO runDAO;

    private final AWSApiClient awsApiClient;

    private final PolicyFileService policyFileService;

    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    private final UriComponentsBuilder urlBuilder;
    private final String applicationHost;

    @Autowired
    public AwsPolicyServerServiceImpl(RunDAO runDAO, ObservationDAO observationDAO,
                                      @Value("${pathmind.application.url}") String applicationURL,
                                      @Value("${pathmind.application.environment}") String environment,
                                      AWSApiClient awsApiClient, PolicyFileService policyFileService,
                                      PolicyServerFilesCreator filesCreator) throws MalformedURLException {
        this.filesCreator = filesCreator;
        this.observationDAO = observationDAO;
        this.runDAO = runDAO;
        this.awsApiClient = awsApiClient;
        this.policyFileService = policyFileService;

        URL url = new URL(applicationURL);
        this.applicationHost = environment + "." + url.getHost();
        this.urlBuilder = UriComponentsBuilder.fromHttpUrl(applicationURL);
    }

//    @Override
//    public String createSchemaYaml(long modelId) {
//        List<Observation> observationsForModel = observationDAO.getObservationsForModel(modelId);
//        return filesCreator.createSchemaYaml(observationsForModel);
//    }

    @Override
    public void saveSchemaYamlFile(String jobId, byte[] schemaYaml) {
        log.debug("Uploading schema.yaml for jobId {}", jobId);
        awsApiClient.fileUpload(jobId + "/schema.yaml", schemaYaml);
    }

    @Override
    public void saveSchemaYamlFile(String jobId, PolicyServerSchema schema) {
        try {
            byte[] yamlString = yamlMapper.writeValueAsBytes(schema);
            log.debug("Generated schema.yaml : \n {}", yamlString);
            this.saveSchemaYamlFile(jobId, yamlString);
        } catch (Exception e) {
            log.error("Failed to save schema.yaml");
        }
    }

    @Override
    public byte[] getSchemaYamlFile(String jobId) {
        return awsApiClient.fileContents(jobId + "/schema.yaml", true);
    }

    @Override
    public void triggerPolicyServerDeployment(Experiment experiment) {
        bestPythonPolicy(experiment)
                .ifPresent(policy -> {
                    final Run run = policy.getRun();
                    DeploymentStatus deploymentStatus = runDAO.policyServerDeployedStatus(run.getId());
                    if (deploymentStatus == DeploymentStatus.NOT_DEPLOYED) {

//                        final String policyFile = MessageFormat.format("{0}/output/{1}/policy_{1}.zip", run.getJobId(), policy.getExternalId());
                        final String policyFile = policyFileService.getPolicyFileLocation(policy.getId());
                        DeploymentMessage message = DeploymentMessage.builder()
                                .s3ModelPath(policyFile)
                                .s3SchemaPath(run.getJobId() + "/schema.yaml")
                                .build();

                        awsApiClient.deployPolicyServer(message);
                        deploymentStatus = runDAO.policyServerDeployedStatus(run.getId(), DeploymentStatus.PENDING);
                    }
                    run.setPolicyServerStatus(deploymentStatus);
                });
    }

    @Override
    public String getPolicyServerUrl(Experiment experiment) {
        return bestPythonPolicy(experiment)
                .map(Policy::getRun)
                .map(run -> {
                    if (getPolicyServerStatus(experiment) == DeploymentStatus.DEPLOYED) {
                        String host = run.getJobId() + "." + applicationHost;
                        UriComponents uriComponents = this.urlBuilder.cloneBuilder().host(host).build();
                        return uriComponents.toUriString();
                    }
                    return null;
                }).orElse(null);
    }

    @Override
    public DeploymentStatus getPolicyServerStatus(Experiment experiment) {
        return bestPythonPolicy(experiment)
                .map(Policy::getRun)
                .map(run -> {
                    DeploymentStatus deploymentStatus = runDAO.policyServerDeployedStatus(run.getId());
                    run.setPolicyServerStatus(deploymentStatus);
                    return deploymentStatus;
                }).orElse(DeploymentStatus.NOT_DEPLOYED);
    }

    private static Optional<Policy> bestPythonPolicy(Experiment experiment) {
        return Optional.of(experiment)
                .filter(e -> e.getTrainingStatusEnum() == RunStatus.Completed)
                .map(Experiment::getBestPolicy)
                .filter(policy -> ModelType.isPythonModel(ModelType.fromValue(policy.getModel().getModelType())));
    }

}
