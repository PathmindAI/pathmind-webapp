package io.skymind.pathmind.services;

import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.policy.RewardScore;
import io.skymind.pathmind.data.utils.PolicyUtils;
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

import java.util.Arrays;
import java.util.List;

@Slf4j
public abstract class TrainingService {
    private static final int MINUTE = 60;
    private static final int HOUR = 60 * 60;

    protected final ExecutionProvider executionProvider;
    protected final RunDAO runDAO;
    protected final ModelDAO modelDAO;
    protected final PolicyDAO policyDAO;
    protected final ExecutionProviderMetaDataDAO executionProviderMetaDataDAO;
    protected ExecutionEnvironment executionEnvironment;

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
        startRun(RunType.DiscoveryRun,
                exp,
                100,
                Arrays.asList(1e-3, 1e-4, 1e-5), // Learning rate
                Arrays.asList(0.9, 0.99), // gamma
                Arrays.asList(64, 128), // batch size
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
                24 * HOUR, // 24 hr
                policy);          // base policy
    }

    private Policy generateTempPolicy(JobSpec spec, Run run) {
        return generateTempPolicy(spec, run, null);
    }

    // We want to create a copy of List<RewardScore> so that the references are unique and one doesn't affect the other.
    protected Policy generateTempPolicy(JobSpec spec, Run run, List<RewardScore> scores) {
        // this is for ui filling gap until ui get a training progress from backend(rescale)
        Policy tempPolicy = new Policy();

        tempPolicy.setAlgorithmEnum(Algorithm.PPO);
        tempPolicy.setRunId(run.getId());
        tempPolicy.setLearningRate(spec.getLearningRates().get(0));
        tempPolicy.setGamma(spec.getGammas().get(0));
        tempPolicy.setBatchSize(spec.getBatchSizes().get(0));
        tempPolicy.setExternalId(PolicyUtils.generatePolicyTempName(tempPolicy, run.getRunType()));
        tempPolicy.setName(PolicyUtils.parsePolicyName(tempPolicy.getExternalId()));
        tempPolicy.setNotes(PolicyUtils.generateDefaultNotes(tempPolicy));

        if(scores != null)
            tempPolicy.setScores(scores);

        return tempPolicy;
    }

    private void startRun(RunType runType, Experiment exp, int iterations, List<Double> learningRates, List<Double> gammas, List<Integer> batchSizes, int maxTimeInSec) {
        startRun(runType, exp, iterations, learningRates, gammas, batchSizes, maxTimeInSec, null);
    }

    protected abstract void startRun(RunType runType, Experiment exp, int iterations, List<Double> learningRates, List<Double> gammas, List<Integer> batchSizes, int maxTimeInSec, Policy basePolicy);
}