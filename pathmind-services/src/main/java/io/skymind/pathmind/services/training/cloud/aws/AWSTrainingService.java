package io.skymind.pathmind.services.training.cloud.aws;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.db.dao.ExecutionProviderMetaDataDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.ModelService;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.shared.services.training.ExecutionProvider;
import io.skymind.pathmind.shared.services.training.JobSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static io.skymind.pathmind.shared.constants.RunType.DiscoveryRun;

@Service
@Slf4j
public class AWSTrainingService extends TrainingService {
    private final boolean multiAgent;
    public AWSTrainingService(@Value("${pathmind.training.multiagent:false}") boolean multiAgent,
                              ExecutionProvider executionProvider, RunDAO runDAO, ModelService modelService,
                              PolicyDAO policyDAO,
                              ExecutionProviderMetaDataDAO executionProviderMetaDataDAO) {
        super(multiAgent, executionProvider, runDAO, modelService, policyDAO, executionProviderMetaDataDAO);
        this.multiAgent = multiAgent;
    }

    protected void startRun(Experiment exp, int iterations, int maxTimeInSec, int numSamples, Policy basePolicy) {
        final Run run = runDAO.createRun(exp, DiscoveryRun);
        // Get model from the database, as the one we can get from the experiment doesn't have all fields
        final Model model = modelService.getModel(exp.getModelId()).get();

        executionProvider.uploadModel(run.getId(), modelService.getModelFile(model.getId()));

        final JobSpec spec = new JobSpec(
                exp.getProject().getPathmindUserId(),
                model.getId(),
                exp.getId(),
                run.getId(),
                null,
                "", // not collected via UI yet
                "",    // not collected via UI yet
                exp.getRewardFunction(),
                model.getNumberOfPossibleActions(),
                model.getNumberOfObservations(),
                iterations,
                executionEnvironment,
                DiscoveryRun,
                maxTimeInSec,
                numSamples,
                multiAgent,
                false,
                50
        );

        if (basePolicy != null) {
            String checkpointFileId = executionProviderMetaDataDAO.getCheckPointFileKey(basePolicy.getExternalId());
            if (checkpointFileId != null) {
                // for AWS provider, need to pass s3 path
                String checkpointS3Path = checkpointFileId + "/output/" + basePolicy.getExternalId() + "/" + "checkpoint.zip";
                spec.setCheckpointFileId(checkpointS3Path);
            }
        }

        // IMPORTANT -> There are multiple database calls within executionProvider.execute.
        final String executionId = executionProvider.execute(spec);
        executionProviderMetaDataDAO.putProviderRunJobId(spec.getRunId(),executionId);

        runDAO.markAsStarting(run.getId());
        log.info("Started {} training job with id {}", DiscoveryRun, executionId);
    }


}
