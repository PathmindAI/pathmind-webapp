package io.skymind.pathmind.services;

import static io.skymind.pathmind.services.training.constant.RunConstants.DISCOVERY_RUN_BATCH_SIZES;
import static io.skymind.pathmind.services.training.constant.RunConstants.DISCOVERY_RUN_GAMMAS;
import static io.skymind.pathmind.services.training.constant.RunConstants.DISCOVERY_RUN_LEARNING_RATES;
import static io.skymind.pathmind.services.training.constant.RunConstants.TRAINING_HYPERPARAMETERS;

import java.util.Arrays;
import java.util.List;

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
import io.skymind.pathmind.services.training.constant.RunConstants;
import io.skymind.pathmind.services.training.versions.AnyLogic;
import io.skymind.pathmind.services.training.versions.PathmindHelper;
import io.skymind.pathmind.services.training.versions.RLLib;
import lombok.extern.slf4j.Slf4j;

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
                15 * MINUTE,
                10
        );
    }

    public void startDiscoveryRun(Experiment exp){
        startRun(RunType.DiscoveryRun,
                exp,
                RunConstants.DISCOVERY_RUN_ITERATIONS,
                (List<Double>) TRAINING_HYPERPARAMETERS.get(DISCOVERY_RUN_LEARNING_RATES), // Learning rate
                (List<Double>) TRAINING_HYPERPARAMETERS.get(DISCOVERY_RUN_GAMMAS), // gamma
                (List<Integer>) TRAINING_HYPERPARAMETERS.get(DISCOVERY_RUN_BATCH_SIZES), // batch size
                30 * MINUTE,
                10
        );
    }

    public void startFullRun(Experiment exp, Policy policy){
        startRun(RunType.FullRun,
                exp,
                RunConstants.FULL_RUN_ITERATIONS,
                Arrays.asList(policy.getLearningRate()),
                Arrays.asList(policy.getGamma()),
                Arrays.asList(policy.getBatchSize()),
                24 * HOUR, // 24 hr
                10,
                policy);          // base policy
    }

    private void startRun(RunType runType, Experiment exp, int iterations, List<Double> learningRates, List<Double> gammas, List<Integer> batchSizes, int maxTimeInSec, int numSamples) {
        startRun(runType, exp, iterations, learningRates, gammas, batchSizes, maxTimeInSec, numSamples, null);
    }

    protected abstract void startRun(RunType runType, Experiment exp, int iterations, List<Double> learningRates, List<Double> gammas, List<Integer> batchSizes, int maxTimeInSec, int numSampes, Policy basePolicy);
}