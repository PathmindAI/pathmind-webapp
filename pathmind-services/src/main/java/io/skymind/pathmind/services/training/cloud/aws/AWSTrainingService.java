package io.skymind.pathmind.services.training.cloud.aws;

import java.util.List;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.constants.ModelType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.shared.data.Run;
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

    public AWSTrainingService(ExecutionEnvironmentManager executionEnvironmentManager,
                              FeatureManager featureManager,
                              ExecutionProvider executionProvider, RunDAO runDAO, ModelService modelService,
                              PolicyDAO policyDAO,
                              ModelDAO modelDAO,
                              ObservationDAO observationDAO,
                              ExperimentDAO experimentDAO,
                              DSLContext ctx) {
        super(executionProvider, runDAO, modelService, executionEnvironmentManager, policyDAO, modelDAO, experimentDAO, ctx);
        this.observationDAO = observationDAO;
        this.featureManager = featureManager;
    }

    protected String startRun(Model model, Experiment exp, Run run, int iterations, int maxTimeInSec, int numSamples) {
        // Get model from the database, as the one we can get from the experiment doesn't have all fields
        final String modelFileId = modelService.buildModelPath(model.getId());
        List<Observation> observations = observationDAO.getObservationsForExperiment(exp.getId());

        String packageName = model.getPackageName();
        String[] split = packageName.split(";");
        String objSelection = null;
        String rewFctName = null;
        if (split.length == 3) {
            packageName = split[0];
            objSelection = !split[1].equals("null") ? split[1] : null;
            rewFctName = !split[2].equals("null") ? split[2] : null;
        }

        final JobSpec spec = new JobSpec(
                exp.getProject().getPathmindUserId(),
                model.getId(),
                exp.getId(),
                run.getId(),
                modelFileId,
                null,
                "", // not collected via UI yet
                "", // not collected via UI yet
                exp.getRewardFunction(),
                "",
                observations,
                iterations,
                executionEnvironment,
                ModelType.fromValue(model.getModelType()),
                DiscoveryRun,
                maxTimeInSec,
                numSamples,
                ModelType.isMultiModel(ModelType.fromValue(model.getModelType())),
                false,
                25,
                false,
                true,
                true,
                StringUtils.defaultString(model.getMainAgent()),
                StringUtils.defaultString(model.getExperimentClass()),
                StringUtils.defaultString(model.getExperimentType()),
                // doesn't need to pass package name for AL model, only need for PY model
                packageName,
                objSelection,
                rewFctName,
                model.isActionmask()
        );

        return executionProvider.execute(spec);
    }
}
