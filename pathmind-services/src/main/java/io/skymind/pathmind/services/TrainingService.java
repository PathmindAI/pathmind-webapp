package io.skymind.pathmind.services;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.db.dao.ExecutionProviderMetaDataDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.shared.services.training.ExecutionEnvironment;
import io.skymind.pathmind.shared.services.training.ExecutionProvider;
import io.skymind.pathmind.shared.services.training.constant.RunConstants;
import io.skymind.pathmind.shared.services.training.versions.AnyLogic;
import io.skymind.pathmind.shared.services.training.versions.Conda;
import io.skymind.pathmind.shared.services.training.versions.JDK;
import io.skymind.pathmind.shared.services.training.versions.NativeRL;
import io.skymind.pathmind.shared.services.training.versions.PathmindHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class TrainingService {
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
//        executionEnvironment = new ExecutionEnvironment(AnyLogic.VERSION_8_5_2, pathmindHelperVersion, NativeRL.VERSION_0_7_6_PBT, JDK.VERSION_8_222, Conda.VERSION_0_7_6);
        // todo revert to 0_7_6_PBT after testing resume
        executionEnvironment = new ExecutionEnvironment(AnyLogic.VERSION_8_5_2, pathmindHelperVersion, NativeRL.VERSION_0_7_6_RESUME, JDK.VERSION_8_222, Conda.VERSION_0_7_6);
    }
    public void startRun(Experiment exp){
        startRun(exp,
                RunConstants.PBT_RUN_ITERATIONS,
                RunConstants.PBT_MAX_TIME_IN_SEC,
                RunConstants.PBT_NUM_SAMPLES
        );
    }

    private void startRun(Experiment exp, int iterations, int maxTimeInSec, int numSamples) {
        runDAO.clearNotificationSentInfo(exp.getId());
        startRun(exp, iterations, maxTimeInSec, numSamples, null);
    }

    protected abstract void startRun(Experiment exp, int iterations, int maxTimeInSec, int numSampes, Policy basePolicy);
}