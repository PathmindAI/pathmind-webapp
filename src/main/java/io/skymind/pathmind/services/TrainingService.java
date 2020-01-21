package io.skymind.pathmind.services;

import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.policy.RewardScore;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.data.utils.RunUtils;
import io.skymind.pathmind.db.dao.ExecutionProviderMetaDataDAO;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.training.ExecutionEnvironment;
import io.skymind.pathmind.services.training.ExecutionProvider;
import io.skymind.pathmind.services.training.JobSpec;
import io.skymind.pathmind.services.training.versions.AnyLogic;
import io.skymind.pathmind.services.training.versions.PathmindHelper;
import io.skymind.pathmind.services.training.versions.RLLib;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrainingService
{
    private static final int MINUTE = 60;

    private static final String PATHMIND_ENVIRONMENT = "PathmindEnvironment";

    private final ExecutionProvider executionProvider;
    private final RunDAO runDAO;
    private final ModelDAO modelDAO;
    private final PolicyDAO policyDAO;
    private final ExecutionProviderMetaDataDAO executionProviderMetaDataDAO;
    private ExecutionEnvironment executionEnvironment;

    public TrainingService(ExecutionProvider executionProvider, RunDAO runDAO, ModelDAO modelDAO, PolicyDAO policyDAO, ExecutionProviderMetaDataDAO executionProviderMetaDataDAO) {
        this.executionProvider = executionProvider;
        this.runDAO = runDAO;
        this.modelDAO = modelDAO;
        this.policyDAO = policyDAO;
        this.executionProviderMetaDataDAO = executionProviderMetaDataDAO;

//        executionEnvironment = new ExecutionEnvironment(AnyLogic.VERSION_8_5, PathmindHelper.VERSION_0_0_24, RLLib.VERSION_0_7_0);
        executionEnvironment = new ExecutionEnvironment(AnyLogic.VERSION_8_5_1, PathmindHelper.VERSION_0_0_24, RLLib.VERSION_0_7_0);
    }

    public void startTestRun(Experiment exp){
        startRun(RunType.TestRun,
                exp,
                50,
                Arrays.asList(1e-5),
                Arrays.asList(0.99),
                Arrays.asList(128),
                15 * MINUTE
        );
    }

    public void startDiscoveryRun(Experiment exp){
        startDiscoveryRunJob1(exp);
        startDiscoveryRunJob2(exp);
    }

    public void startDiscoveryRunJob1(Experiment exp) {
        startRun(RunType.DiscoveryRun,
                exp,
                100,
                Arrays.asList(1e-3, 1e-5), // Learning rate
                Arrays.asList(0.9, 0.99), // gamma
                Arrays.asList(64), // batch size
                30 * MINUTE
        );
    }

    public void startDiscoveryRunJob2(Experiment exp) {
        startRun(RunType.DiscoveryRun,
                exp,
                100,
                Arrays.asList(1e-3, 1e-5), // Learning rate
                Arrays.asList(0.9, 0.99), // gamma
                Arrays.asList(128), // batch size
                30 * MINUTE
        );
    }

    public void startFullRun(Experiment exp, Policy policy){
        startRun(RunType.FullRun,
                exp,
                500,
                Arrays.asList(policy.getLearningRate()),
                Arrays.asList(policy.getGamma()),
                Arrays.asList(policy.getBatchSize()),
                -1, // no limit
                policy);          // base policy
    }

    private Policy generateTempPolicy(JobSpec spec, Run run) {
        return generateTempPolicy(spec, run, null);
    }

    // We want to create a copy of List<RewardScore> so that the references are unique and one doesn't affect the other.
    private Policy generateTempPolicy(JobSpec spec, Run run, List<RewardScore> scores) {
        // this is for ui filling gap until ui get a training progress from backend(rescale)
        Policy tempPolicy = new Policy();

        tempPolicy.setAlgorithmEnum(Algorithm.PPO);
        tempPolicy.setRunId(run.getId());
        tempPolicy.setLearningRate(spec.getLearningRates().get(0));
        tempPolicy.setGamma(spec.getGammas().get(0));
        tempPolicy.setBatchSize(spec.getBatchSizes().get(0));
        tempPolicy.setExternalId(getTempPolicyName(tempPolicy, run.getRunType()));
        tempPolicy.setName(PolicyUtils.parsePolicyName(tempPolicy.getExternalId()));
        tempPolicy.setNotes(PolicyUtils.generateDefaultNotes(tempPolicy));

        if(scores != null)
            tempPolicy.setScores(scores);

        return tempPolicy;
    }

    private String getTempPolicyName(Policy policy, int runType) {
        String hyperparameters = String.join(
                ",",
                "gamma=" + policy.getGamma(),
                "lr=" + policy.getLearningRate(),
                "sgd_minibatch_size=" + policy.getBatchSize());

        String name = String.join(
                "_",
                policy.getAlgorithm(),
                PATHMIND_ENVIRONMENT,
                "0",
                hyperparameters,
                runType + RunUtils.TEMPORARY_POSTFIX);

        return name;
    }

    private void startRun(RunType runType, Experiment exp, int iterations, List<Double> learningRates, List<Double> gammas, List<Integer> batchSizes, int maxTimeInSec) {
        startRun(runType, exp, iterations, learningRates, gammas, batchSizes, maxTimeInSec, null);
    }

    private void startRun(RunType runType, Experiment exp, int iterations, List<Double> learningRates, List<Double> gammas, List<Integer> batchSizes, int maxTimeInSec, Policy basePolicy) {
        final Run run = runDAO.createRun(exp, runType);
        // Get model from the database, as the one we can get from the experiment doesn't have all fields
        final Model model = modelDAO.getModel(exp.getModelId());

        // Get model file id, either uploading it if necessary, or just getting it from the metadata database table
        String modelFileId = executionProviderMetaDataDAO.getModelFileKey(exp.getModelId());
        if (modelFileId == null) {
            modelFileId = executionProvider.uploadModel(modelDAO.getModelFile(model.getId()));
            executionProviderMetaDataDAO.putModelFileKey(exp.getModelId(), modelFileId);
        }

        final JobSpec spec = new JobSpec(
                exp.getProject().getPathmindUserId(),
                model.getId(),
                exp.getId(),
                run.getId(),
                modelFileId,
                "", // not collected via UI yet
                "",    // not collected via UI yet
                exp.getRewardFunction(),
                model.getNumberOfPossibleActions(),
                model.getNumberOfObservations(),
                iterations,
                executionEnvironment,
                runType,
                learningRates,
                gammas,
                batchSizes,
                maxTimeInSec
        );

        List<RewardScore> rewardScores = null;
        if (basePolicy != null) {
            // We want to create a copy of List<RewardScore> so that the references are unique and one doesn't affect the other.
            rewardScores = basePolicy.getScores().stream()
                    .map(score -> new RewardScore(score.getMax(), score.getMin(), score.getMean(), score.getIteration()))
                    .collect(Collectors.toList());

            String checkpointFileId = executionProviderMetaDataDAO.getCheckPointFileKey(basePolicy.getExternalId());
            if (checkpointFileId == null) {
                checkpointFileId = executionProvider.uploadCheckpoint(policyDAO.getSnapshotFile(basePolicy.getId()));
                executionProviderMetaDataDAO.putCheckPointFileKey(basePolicy.getExternalId(), checkpointFileId);
            }

            spec.setCheckpointFileId(checkpointFileId);
        }

        // IMPORTANT -> There are multiple database calls within executionProvider.execute.
        final String executionId = executionProvider.execute(spec);
        executionProviderMetaDataDAO.putRescaleRunJobId(spec.getRunId(),executionId);

        runDAO.markAsStarting(run.getId());
        log.info("Started " + runType + " training job with id {}", executionId);

        policyDAO.insertPolicy(generateTempPolicy(spec, run, rewardScores));
    }
}