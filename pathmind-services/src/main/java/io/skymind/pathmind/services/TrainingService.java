package io.skymind.pathmind.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import io.skymind.pathmind.db.dao.ExecutionProviderMetaDataDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Data;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.ProviderJobStatus;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.services.training.ExecutionEnvironment;
import io.skymind.pathmind.shared.services.training.ExecutionProvider;
import io.skymind.pathmind.shared.services.training.constant.RunConstants;
import io.skymind.pathmind.shared.services.training.versions.*;
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

        PathmindHelper pathmindHelperVersion = PathmindHelper.VERSION_1_0_1;
        if (multiAgent) {
            pathmindHelperVersion = PathmindHelper.VERSION_0_0_25_Multi;
        }

//        executionEnvironment = new ExecutionEnvironment(AnyLogic.VERSION_8_5_2, pathmindHelperVersion, NativeRL.VERSION_0_7_6, JDK.VERSION_8_222, Conda.VERSION_0_7_6);
        executionEnvironment = new ExecutionEnvironment(AnyLogic.VERSION_8_5_2, pathmindHelperVersion, NativeRL.VERSION_1_0_1, JDK.VERSION_8_222, Conda.VERSION_0_7_6);
    }
    
    @Transactional
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

    public void stopRun(Experiment experiment)  {
        List<Run> runs = experiment.getRuns().stream()
                .filter(r -> RunStatus.isRunning(r.getStatusEnum()))
                .collect(Collectors.toList());
        List<Long> runsIds = runs.stream().map(Data::getId).collect(Collectors.toList());
        Map<Long, String> runJobIds = executionProviderMetaDataDAO.getProviderRunJobIds(runsIds);
        for (String jobId: runJobIds.values()) {
            executionProvider.stop(jobId);
        }
        // immediately mark the job as stopping so that the user don't have to wait the updater to update the run
        // status
        runs.forEach(run -> {
            // the 3 lines below are there just to avoid an exception after the status is updated
            run.setExperiment(experiment);
            run.setModel(experiment.getModel());
            run.setProject(experiment.getProject());
            runDAO.updateRun(run, ProviderJobStatus.STOPPING, experiment.getPolicies());
        });
    }
}