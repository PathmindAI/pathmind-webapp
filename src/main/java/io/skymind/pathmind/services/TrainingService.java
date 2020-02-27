package io.skymind.pathmind.services;

import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.db.dao.ExecutionProviderMetaDataDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.training.ExecutionEnvironment;
import io.skymind.pathmind.services.training.ExecutionProvider;
import io.skymind.pathmind.services.training.constant.RunConstants;
import io.skymind.pathmind.services.training.versions.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class TrainingService {
    private static final int MINUTE = 60;
    private static final int HOUR = 60 * 60;

    protected final ExecutionProvider executionProvider;
    protected final RunDAO runDAO;
    protected final ModelService modelService;
    protected final PolicyDAO policyDAO;
    protected final ExecutionProviderMetaDataDAO executionProviderMetaDataDAO;
    protected ExecutionEnvironment executionEnvironment;

    public TrainingService(boolean multiAgent, ExecutionProvider executionProvider,
                           RunDAO runDAO, ModelService modelService,
                           PolicyDAO policyDAO,
                           ExecutionProviderMetaDataDAO executionProviderMetaDataDAO) {
        this.executionProvider = executionProvider;
        this.runDAO = runDAO;
        this.modelService = modelService;
        this.policyDAO = policyDAO;
        this.executionProviderMetaDataDAO = executionProviderMetaDataDAO;

        PathmindHelper pathmindHelperVersion = PathmindHelper.VERSION_0_0_25;
        if (multiAgent) {
            pathmindHelperVersion = PathmindHelper.VERSION_0_0_25_Multi;
        }

//        executionEnvironment = new ExecutionEnvironment(AnyLogic.VERSION_8_5_2, pathmindHelperVersion, NativeRL.VERSION_0_7_6, JDK.VERSION_8_222, Conda.VERSION_0_7_6);
        executionEnvironment = new ExecutionEnvironment(AnyLogic.VERSION_8_5_2, pathmindHelperVersion, NativeRL.VERSION_0_7_6_PBT, JDK.VERSION_8_222, Conda.VERSION_0_7_6);
    }

    public void startTestRun(Experiment exp){
        startRun(RunType.TestRun,
                exp,
                50,
                15 * MINUTE,
                10
        );
    }

    public void startDiscoveryRun(Experiment exp){
        startRun(RunType.DiscoveryRun,
                exp,
                RunConstants.DISCOVERY_RUN_ITERATIONS,
                30 * MINUTE,
                10
        );
    }

    public void startFullRun(Experiment exp, Policy policy){
        startRun(RunType.FullRun,
                exp,
                RunConstants.FULL_RUN_ITERATIONS,
                24 * HOUR, // 24 hr
                10,
                policy);          // base policy
    }

    private void startRun(RunType runType, Experiment exp, int iterations, int maxTimeInSec, int numSamples) {
        runDAO.clearNotificationSentInfo(exp.getId(), runType.getValue());
        startRun(runType, exp, iterations, maxTimeInSec, numSamples, null);
    }

    protected abstract void startRun(RunType runType, Experiment exp, int iterations, int maxTimeInSec, int numSampes, Policy basePolicy);
}