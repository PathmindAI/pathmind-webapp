package io.skymind.pathmind.services.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.services.PolicyServerFilesCreator;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import io.skymind.pathmind.services.training.cloud.aws.api.dto.DeploymentMessage;
import io.skymind.pathmind.shared.constants.ModelType;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.services.PolicyServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@Slf4j
@Service
class AwsPolicyServerServiceImpl implements PolicyServerService {

    private final PolicyServerFilesCreator filesCreator;

    private final ObservationDAO observationDAO;

    private final RunDAO runDAO;

    private final UserDAO userDAO;

    private final AWSApiClient awsApiClient;

    private final PolicyFileService policyFileService;

    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory().enable(YAMLGenerator.Feature.MINIMIZE_QUOTES));

    private final UriComponentsBuilder urlBuilder;

    @Autowired
    public AwsPolicyServerServiceImpl(RunDAO runDAO, ObservationDAO observationDAO, UserDAO userDAO,
                                      @Value("${pathmind.application.url}") String applicationUrl,
                                      @Value("${pathmind.application.environment}") String environment,
                                      AWSApiClient awsApiClient, PolicyFileService policyFileService,
                                      PolicyServerFilesCreator filesCreator) throws MalformedURLException {
        this.filesCreator = filesCreator;
        this.observationDAO = observationDAO;
        this.runDAO = runDAO;
        this.userDAO = userDAO;
        this.awsApiClient = awsApiClient;
        this.policyFileService = policyFileService;

        final URL url = new URL(applicationUrl);
        String policyServerURI;
        if (environment.equalsIgnoreCase("prod")) {
            policyServerURI = url.getHost().replaceFirst("app", "api");
        } else {
            policyServerURI = "api." + url.getHost();
        }
        log.debug("Serve policy at {}", policyServerURI);
        this.urlBuilder = UriComponentsBuilder.fromHttpUrl(applicationUrl).host(policyServerURI);
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
    public PolicyServerSchema generateSchemaYaml(Run run) {

        run = runDAO.getRun(run.getId()); // here run comes not full
        long userId = run.getProject().getPathmindUserId();
        PathmindUser user = userDAO.findById(userId);

        final boolean isPythonModel = ModelType.isPythonModel(ModelType.fromValue(run.getModel().getModelType()));

        PolicyServerService.PolicyServerSchema.PolicyServerSchemaBuilder schemaBuilder = PolicyServerService.PolicyServerSchema.builder();
        schemaBuilder
                .parameters(
                        PolicyServerService.PolicyServerSchema.Parameters.builder()
                                .discrete(isPythonModel ? false : true)
                                .tuple(isPythonModel ? false : true)
                                .apiKey(user.getApiKey())
                                .urlPath("policy/" + run.getJobId())
                                .build()
                );

        schemaBuilder.observations(observationDAO.getObservationsForModel(run.getModel().getId()));

        return schemaBuilder.build();
    }

    @Override
    public void triggerPolicyServerDeployment(Experiment experiment) {
        operatePolicyServer(experiment, false);
    }

    @Override
    public void destroyPolicyServerDeployment(Experiment experiment) {
        operatePolicyServer(experiment, true);
    }

    @Override
    public String getPolicyServerUrl(Experiment experiment) {
        return bestPolicyIfCompleted(experiment)
                .map(Policy::getRun)
                .map(run -> {
                    if (getPolicyServerStatus(experiment) == DeploymentStatus.DEPLOYED) {
                        UriComponents uriComponents = this.urlBuilder.cloneBuilder()
                                .pathSegment("policy", run.getJobId())
                                .build();
                        return uriComponents.toUriString();
                    }
                    return null;
                }).orElse(null);
    }

    @Override
    public DeploymentStatus getPolicyServerStatus(Experiment experiment) {
        return bestPolicyIfCompleted(experiment)
                .map(Policy::getRun)
                .map(run -> {
                    DeploymentStatus deploymentStatus = runDAO.policyServerDeployedStatus(run.getId());
                    run.setPolicyServerStatus(deploymentStatus);
                    return deploymentStatus;
                }).orElse(DeploymentStatus.NOT_DEPLOYED);
    }

    private static Optional<Policy> bestPolicyIfCompleted(Experiment experiment) {
        return Optional.of(experiment)
                .filter(e -> e.getTrainingStatusEnum() == RunStatus.Completed)
                .map(Experiment::getBestPolicy);
    }

    private void operatePolicyServer(Experiment experiment, boolean destroy) {
        bestPolicyIfCompleted(experiment)
                .ifPresent(policy -> {
                    final Run run = policy.getRun();
                    DeploymentStatus deploymentStatus = runDAO.policyServerDeployedStatus(run.getId());

                    DeploymentMessage.DeploymentMessageBuilder message =
                            DeploymentMessage.builder()
                                    .jobId(run.getJobId());

                    if (DeploymentStatus.DEPLOYABLE.contains(deploymentStatus)) {

                        PolicyServerService.PolicyServerSchema schema = generateSchemaYaml(run);
                        saveSchemaYamlFile(run.getJobId(), schema);

                        final String policyFile = policyFileService.getPolicyFileLocation(policy.getId());
                        message
                                .s3ModelPath(policyFile)
                                .s3SchemaPath(run.getJobId() + "/schema.yaml");;

                        deploymentStatus = runDAO.updatePolicyServerDeployedStatus(run.getId(), DeploymentStatus.PENDING);
                        run.setPolicyServerStatus(deploymentStatus);
                    } else if (DeploymentStatus.DEPLOYED == deploymentStatus && destroy) {
                        message.destroy("1");
                    }

                    awsApiClient.operatePolicyServer(message.build());
                });
    }

}
