package io.skymind.pathmind.services.training.cloud.aws;

import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.policy.RewardScore;
import io.skymind.pathmind.db.dao.ExecutionProviderMetaDataDAO;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.services.training.ExecutionProvider;
import io.skymind.pathmind.services.training.JobSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AWSTrainingService extends TrainingService {
    private final boolean multiAgent;
    public AWSTrainingService(@Value("${pathmind.training.multiagent:false}") boolean multiAgent,
                              ExecutionProvider executionProvider, RunDAO runDAO, ModelDAO modelDAO, PolicyDAO policyDAO,
                              ExecutionProviderMetaDataDAO executionProviderMetaDataDAO) {
        super(multiAgent, executionProvider, runDAO, modelDAO, policyDAO, executionProviderMetaDataDAO);
        this.multiAgent = multiAgent;
    }

    protected void startRun(RunType runType, Experiment exp, int iterations, int maxTimeInSec, int numSamples, Policy basePolicy) {
        final Run run = runDAO.createRun(exp, runType);
        // Get model from the database, as the one we can get from the experiment doesn't have all fields
        final Model model = modelDAO.getModel(exp.getModelId()).get();

        executionProvider.uploadModel(run.getId(), modelDAO.getModelFile(model.getId()));

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
                runType,
                maxTimeInSec,
                numSamples,
                multiAgent
        );

        List<RewardScore> rewardScores = null;
        if (basePolicy != null) {
            // We want to create a copy of List<RewardScore> so that the references are unique and one doesn't affect the other.
            rewardScores = basePolicy.getScores().stream()
                    .map(score -> new RewardScore(score.getMax(), score.getMin(), score.getMean(), score.getIteration()))
                    .collect(Collectors.toList());

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
        log.info("Started " + runType + " training job with id {}", executionId);

//        policyDAO.insertPolicy(generateTempPolicy(spec, run, rewardScores));
    }


}
