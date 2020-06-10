package io.skymind.pathmind.services.training.cloud.aws;

import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.shared.services.training.ExecutionProvider;
import io.skymind.pathmind.shared.services.training.JobSpec;
import io.skymind.pathmind.shared.services.training.environment.ExecutionEnvironmentManager;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static io.skymind.pathmind.shared.constants.RunType.DiscoveryRun;

@Service
@Slf4j
public class AWSTrainingService extends TrainingService {
    private final FeatureManager featureManager;
    public AWSTrainingService(ExecutionEnvironmentManager executionEnvironmentManager,
                              FeatureManager featureManager,
                              ExecutionProvider executionProvider, RunDAO runDAO, ModelService modelService,
                              PolicyDAO policyDAO,
                              ModelDAO modelDAO,
                              DSLContext ctx) {
    	super(executionProvider, runDAO, modelService, executionEnvironmentManager, policyDAO, modelDAO, ctx);
    	this.featureManager = featureManager;
    }

    protected String startRun(Model model, Experiment exp, Run run, int iterations, int maxTimeInSec, int numSamples) {
        // Get model from the database, as the one we can get from the experiment doesn't have all fields
        final String modelFileId = modelService.buildModelPath(model.getId());

        final JobSpec spec = new JobSpec(
                exp.getProject().getPathmindUserId(),
                model.getId(),
                exp.getId(),
                run.getId(),
                modelFileId,
                "", // not collected via UI yet
                "", // not collected via UI yet
                exp.getRewardFunction(),
                model.getNumberOfPossibleActions(),
                model.getNumberOfObservations(),
                iterations,
                executionEnvironment,
                DiscoveryRun,
                maxTimeInSec,
                numSamples,
                featureManager.isEnabled(Feature.MULTI_AGENT_TRAINING),
                false,
                50,
                false
        );

        return executionProvider.execute(spec);        
    }


}
