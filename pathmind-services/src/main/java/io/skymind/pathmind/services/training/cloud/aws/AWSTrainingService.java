package io.skymind.pathmind.services.training.cloud.aws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.db.dao.SimulationParameterDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.constants.ModelType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.data.SimulationParameter;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.shared.services.training.ExecutionProvider;
import io.skymind.pathmind.shared.services.training.JobSpec;
import io.skymind.pathmind.shared.services.training.environment.ExecutionEnvironmentManager;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.shared.utils.ObservationUtils;
import io.skymind.pathmind.shared.utils.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static io.skymind.pathmind.shared.constants.RunType.DiscoveryRun;

@Service
@Slf4j
public class AWSTrainingService extends TrainingService {
    private final FeatureManager featureManager;
    private final ObservationDAO observationDAO;
    private final SimulationParameterDAO simulationParameterDAO;

    public AWSTrainingService(ExecutionEnvironmentManager executionEnvironmentManager,
                              FeatureManager featureManager,
                              ExecutionProvider executionProvider, RunDAO runDAO, ModelService modelService,
                              PolicyDAO policyDAO,
                              ModelDAO modelDAO,
                              ObservationDAO observationDAO,
                              ExperimentDAO experimentDAO,
                              SimulationParameterDAO simulationParameterDAO,
                              DSLContext ctx) {
        super(executionProvider, runDAO, modelService, executionEnvironmentManager, policyDAO, modelDAO, experimentDAO, ctx);
        this.observationDAO = observationDAO;
        this.featureManager = featureManager;
        this.simulationParameterDAO = simulationParameterDAO;
    }

    protected String startRun(Model model, Experiment exp, Run run, int iterations, int maxTimeInSec, int numSamples) {
        // Get model from the database, as the one we can get from the experiment doesn't have all fields
        final long modelId = model.getId();
        final ModelType modelType = ModelType.fromValue(model.getModelType());
        List<Observation> observations = observationDAO.getObservationsForExperiment(exp.getId());
        List<SimulationParameter> simulationParameters = simulationParameterDAO.getSimulationParametersForExperiment(exp.getId());

        String packageName = model.getPackageName();
        String[] split = packageName.split(";");
        String objSelection = null;
        String rewFctName = null;
        if (split.length == 3) {
            packageName = split[0];
            objSelection = !split[1].equals("null") ? split[1] : null;
            rewFctName = !split[2].equals("null") ? split[2] : null;
        }


        String modelFileId = modelService.buildModelPath(modelId);
        if (ModelType.isPathmindModel(modelType)) {
            String obsYaml = ObservationUtils.toYaml(observations);

            byte[] originalModel = modelService.getModelFile(modelId);
            try (ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(originalModel))) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream zos = new ZipOutputStream(baos);
                ZipEntry entry = null;
                while ((entry = zipStream.getNextEntry()) != null) {
                    if (!entry.getName().equalsIgnoreCase("obs.yaml")) {
                        zos.putNextEntry(entry);
                        byte[] entryBytes = ZipUtils.entryContentExtractor().apply(zipStream, entry);
                        zos.write(entryBytes);
                        zos.closeEntry();
                    }
                    zipStream.closeEntry();
                }
                zos.putNextEntry(new ZipEntry("obs.yaml"));
                zos.write(obsYaml.getBytes(Charset.defaultCharset()));
                zos.closeEntry();
                zos.close();
                byte[] modifiedModel = baos.toByteArray();
                modelFileId = modelService.buildModelPath(modelId, exp.getId());
                modelService.saveModelFile(modelId, exp.getId(), modifiedModel);
            } catch (IOException e) {
                log.error("Not able to process entry", e);
            }
        }

        final JobSpec.JobSpecBuilder spec = JobSpec.builder()
                .reward(exp.getRewardFunctionFromTerms())
                .userId(exp.getProject().getPathmindUserId())
                .modelId(modelId)
                .experimentId(exp.getId())
                .runId(run.getId())
                .modelFileId(modelFileId)
                .checkpointFileId(null)
                .variables("") // not collected via UI yet
                .reset("") // not collected via UI yet
                .metrics("")
                .selectedObservations(observations)
                .simulationParameters(simulationParameters)
                .iterations(iterations)
                .env(executionEnvironment)
                .modelType(modelType)
                .type(DiscoveryRun)
                .maxTimeInSec(maxTimeInSec)
                .numSamples(numSamples)
                .multiAgent(ModelType.isMultiModel(modelType))
                .resume(false)
                .checkpointFrequency(25)
                .recordMetricsRaw(true)
                .namedVariables(true)
                .mainAgentName(StringUtils.defaultString(model.getMainAgent()))
                .expClassName(StringUtils.defaultString(model.getExperimentClass()))
                .expClassType(StringUtils.defaultString(model.getExperimentType()))
                // doesn't need to pass package name for AL model, only need for PY model
                .environment(packageName)
                .obsSelection(objSelection)
                .rewFctName(rewFctName)
                .actionMask(model.isActionmask());

        if (exp.isWithRewardTerms()) {
            spec.termsWeight(ExperimentUtils.rewardTermsWeights(exp));
        }

        return executionProvider.execute(spec.build());
    }
}
