package io.skymind.pathmind.services.training.cloud.aws;

import java.util.List;

import io.skymind.pathmind.db.dao.*;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.constants.ModelType;
import io.skymind.pathmind.shared.data.*;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.shared.services.training.ExecutionProvider;
import io.skymind.pathmind.shared.services.training.JobSpec;
import io.skymind.pathmind.shared.services.training.environment.ExecutionEnvironmentManager;
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
        final String modelFileId = modelService.buildModelPath(model.getId());
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

        final JobSpec spec = JobSpec.builder()
                .userId(exp.getProject().getPathmindUserId())
                .modelId(model.getId())
                .experimentId(exp.getId())
                .runId(run.getId())
                .modelFileId(modelFileId)
                .checkpointFileId(null)
                .variables("") // not collected via UI yet
                .reset("") // not collected via UI yet
                .reward(exp.getRewardFunction())
                .metrics("")
                .selectedObservations(observations)
                .simulationParameters(simulationParameters)
                .iterations(iterations)
                .env(executionEnvironment)
                .modelType(ModelType.fromValue(model.getModelType()))
                .type(DiscoveryRun)
                .maxTimeInSec(maxTimeInSec)
                .numSamples(numSamples)
                .multiAgent(ModelType.isMultiModel(ModelType.fromValue(model.getModelType())))
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
                .actionMask(model.isActionmask())
                .build();

        return executionProvider.execute(spec);
    }
}
