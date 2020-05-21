package io.skymind.pathmind.services;

import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.shared.constants.EC2InstanceType;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.services.training.ExecutionEnvironment;
import io.skymind.pathmind.shared.services.training.ExecutionProvider;
import io.skymind.pathmind.shared.services.training.constant.RunConstants;
import io.skymind.pathmind.shared.services.training.versions.*;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static io.skymind.pathmind.shared.constants.RunType.DiscoveryRun;

@Slf4j
public abstract class TrainingService {
    protected final ExecutionProvider executionProvider;
    protected final RunDAO runDAO;
    protected final ModelService modelService;
    protected final PolicyDAO policyDAO;
    protected ExecutionEnvironment executionEnvironment;
    private final DSLContext ctx;

    public TrainingService(boolean multiAgent, ExecutionProvider executionProvider,
                           RunDAO runDAO, ModelService modelService,
                           PolicyDAO policyDAO, DSLContext ctx) {
        this.executionProvider = executionProvider;
        this.runDAO = runDAO;
        this.modelService = modelService;
        this.policyDAO = policyDAO;
        this.ctx = ctx;

        PathmindHelper pathmindHelperVersion = PathmindHelper.VERSION_1_0_1;
        if (multiAgent) {
            pathmindHelperVersion = PathmindHelper.VERSION_0_0_25_Multi;
        }

        executionEnvironment = new ExecutionEnvironment(AnyLogic.VERSION_8_5_2,
                pathmindHelperVersion, NativeRL.VERSION_1_0_6,
                JDK.VERSION_8_222, Conda.VERSION_0_7_6, EC2InstanceType.IT_16CPU_32GB);
    }
    
    public void startRun(Experiment exp){
        startRun(exp,
                RunConstants.PBT_RUN_ITERATIONS,
                RunConstants.PBT_MAX_TIME_IN_SEC,
                RunConstants.PBT_NUM_SAMPLES
        );
    }

    private void startRun(Experiment exp, int iterations, int maxTimeInSec, int numSamples) {
    	ctx.transaction(conf -> {
    		Run run = runDAO.createRun(conf, exp, DiscoveryRun);
    		String executionId = startRun(exp.getModel(), exp, run, iterations, maxTimeInSec, numSamples);
    		runDAO.markAsStarting(conf, run.getId(), executionId);
            log.info("Started {} training job with id {}", DiscoveryRun, executionId);
    	});
    }

    protected abstract String startRun(Model model, Experiment exp, Run run, int iterations, int maxTimeInSec, int numSampes);

    public void stopRun(Experiment experiment, BiConsumer<Run, List<Policy>> callback)  {
        List<Run> runs = experiment.getRuns().stream()
                .filter(r -> RunStatus.isRunning(r.getStatusEnum()))
                .collect(Collectors.toList());
        
        runs.stream().map(Run::getJobId).forEach(executionProvider::stop);

        // immediately mark the job as stopping so that the user doesn't have to wait the updater to update the run
        // status
        runs.forEach(run -> {
            // the 3 lines below are there just to avoid an exception after the status is updated
            run.setExperiment(experiment);
            run.setModel(experiment.getModel());
            run.setProject(experiment.getProject());
            runDAO.markAsStopping(run);
            if (callback != null) {
                callback.accept(run, experiment.getPolicies());
            }
        });
    }
}