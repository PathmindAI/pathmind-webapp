package io.skymind.pathmind.services;

import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;
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
import org.jooq.JSONB;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class TrainingService {
    private final ExecutionProvider executionProvider;
    private final RunDAO runDAO;
    private final ModelDAO modelDAO;
    private final PolicyDAO policyDAO;
    private final ExecutionProviderMetaDataDAO executionProviderMetaDataDAO;
    private ExecutionEnvironment executionEnvironment;

    private static final int MINUTE = 60;

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
                15 * MINUTE,
                true);
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
                30 * MINUTE,
                false);
    }

    public void startDiscoveryRunJob2(Experiment exp) {
        startRun(RunType.DiscoveryRun,
                exp,
                100,
                Arrays.asList(1e-3, 1e-5), // Learning rate
                Arrays.asList(0.9, 0.99), // gamma
                Arrays.asList(128), // batch size
                30 * MINUTE,
                true);
    }

    public void startFullRun(Experiment exp, Policy policy){
        startRun(RunType.FullRun,
                exp,
                500,
                Arrays.asList(policy.getHyperParameters().getLearningRate()),
                Arrays.asList(policy.getHyperParameters().getGamma()),
                Arrays.asList(policy.getHyperParameters().getBatchSize()),
                -1, // no limit
                true);

//        final JSONB progress = policyDAO.getProgress(policy.getId());
//
//        spec.setSnapshot(() -> policyDAO.getSnapshotFile(policy.getId()));
//        spec.setParentPolicyExternalId(policy.getExternalId());
//
//        final String executionId = executionProvider.execute(spec);
//
//        runDAO.markAsStarting(run.getId());
//        log.info("Started FULL training job with id {}", executionId);
//
//        addTempPolicy(spec, run, progress);
    }

    private Policy generateTempPolicy(JobSpec spec, Run run) {
        return generateTempPolicy(spec, run, null);
    }

    private Policy generateTempPolicy(JobSpec spec, Run run, JSONB progress) {
        // this is for ui filling gap until ui get a training progress from backend(rescale)
        Policy tempPolicy = new Policy();

        String name = getTempPolicyName(Algorithm.PPO.toString(),
                "PathmindEnvironment",
                spec.getLearningRates(),
                spec.getGammas(),
                spec.getBatchSizes(),
                run.getRunType());

        tempPolicy.setAlgorithmEnum(Algorithm.PPO);
        tempPolicy.setName(name);
        tempPolicy.setExternalId(name);
        tempPolicy.setRunId(run.getId());

        if (progress != null) {
            tempPolicy.setProgress(progress.toString());
        }

        return tempPolicy;
    }

    // STEPH -> REFACTOR -> This should be in the DAO layer and not the service layer as this is information on how data is stored
    // within the database. However for now I'm just quickly putting it here so that we can process the PR asap.
    private String getTempPolicyName(String algorithm, String environment, List<Double> lrs, List<Double> gammas, List<Integer> batchSize, int runType) {
        String hyperparameters = String.join(
                ",",
                "gamma=" + gammas.get(0),
                "lr=" + lrs.get(0),
                "sgd_minibatch_size=" + batchSize.get(0)
        );

        String name = String.join(
                "_",
                algorithm,
                environment,
                "0",
                hyperparameters,
                runType + RunUtils.TEMPORARY_POSTFIX
        );

        return name;
    }

    private void startRun(RunType runType, Experiment exp, int iterations, List<Double> learningRates, List<Double> gammas, List<Integer> batchSizes, int maxTimeInSec, boolean isAddTempPolicy)
    {
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

        // IMPORTANT -> There are multiple database calls within executionProvider.execute.
        final String executionId = executionProvider.execute(spec);
        executionProviderMetaDataDAO.putRescaleRunJobId(spec.getRunId(),executionId);

        runDAO.markAsStarting(run.getId());
        log.info("Started " + runType + " training job with id {}", executionId);

        if(isAddTempPolicy)
            policyDAO.insertPolicy(generateTempPolicy(spec, run));
    }
}