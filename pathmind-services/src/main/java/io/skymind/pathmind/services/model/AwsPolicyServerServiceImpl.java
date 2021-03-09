package io.skymind.pathmind.services.model;

import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.PolicyServerFilesCreator;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import io.skymind.pathmind.shared.constants.ModelType;
import io.skymind.pathmind.shared.data.Experiment;
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

    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    private final UriComponentsBuilder urlBuilder;
    private final String applicationHost;

    @Autowired
    public AwsPolicyServerServiceImpl(RunDAO runDAO, ObservationDAO observationDAO,
                                      @Value("${pathmind.application.url}") String applicationURL,
                                      @Value("${pathmind.application.environment}") String environment,
                                      AWSApiClient awsApiClient,
                                      PolicyServerFilesCreator filesCreator) throws MalformedURLException {
        this.filesCreator = filesCreator;
        this.observationDAO = observationDAO;
        this.runDAO = runDAO;
        this.awsApiClient = awsApiClient;

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
        experiment.bestPolicyRun()
                .filter(run -> ModelType.isPythonModel(ModelType.fromValue(run.getModel().getModelType()))) // only PY
                .ifPresent(run -> {
                    DeploymentStatus deploymentStatus = runDAO.policyServerDeployedStatus(run.getId());
                    if (deploymentStatus == DeploymentStatus.NOT_DEPLOYED) {
                        awsApiClient.deployPolicyServer(run.getJobId());
                        deploymentStatus = runDAO.policyServerDeployedStatus(run.getId(), DeploymentStatus.PENDING);
                    }
                    run.setPolicyServerStatus(deploymentStatus);
                });
    }

    @Override
    public String getPolicyServerUrl(Experiment experiment) {
        return experiment.bestPolicyRun()
                .filter(run -> ModelType.isPythonModel(ModelType.fromValue(run.getModel().getModelType()))) // only PY
                .map(run -> {
                    if (run.getPolicyServerStatus() != DeploymentStatus.DEPLOYED) {
                        DeploymentStatus deploymentStatus = runDAO.policyServerDeployedStatus(run.getId());
                        run.setPolicyServerStatus(deploymentStatus);
                    }
                    if (run.getPolicyServerStatus() == DeploymentStatus.DEPLOYED) {
                        String  host = run.getJobId() + "." + applicationHost;
                        UriComponents uriComponents = this.urlBuilder.cloneBuilder().host(host).build();
                        return uriComponents.toUriString();
                    }
                    return null;
                }).orElse(null);
    }

    @Override
    public DeploymentStatus getPolicyServerStatus(Experiment experiment) {
        return experiment.bestPolicyRun()
                .filter(run -> ModelType.isPythonModel(ModelType.fromValue(run.getModel().getModelType()))) // only PY
                .map(run -> {
                    DeploymentStatus deploymentStatus = runDAO.policyServerDeployedStatus(run.getId());
                    run.setPolicyServerStatus(deploymentStatus);
                    return deploymentStatus;
                }).orElse(DeploymentStatus.NOT_DEPLOYED);
    }
}
