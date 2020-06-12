package io.skymind.pathmind.services;

import static io.skymind.pathmind.shared.constants.RunType.DiscoveryRun;

import java.util.function.BiConsumer;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;

import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.services.training.ExecutionProvider;
import io.skymind.pathmind.shared.services.training.constant.RunConstants;
import io.skymind.pathmind.shared.services.training.environment.ExecutionEnvironment;
import io.skymind.pathmind.shared.services.training.environment.ExecutionEnvironmentManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class TrainingService {
    protected final ExecutionProvider executionProvider;
    protected final RunDAO runDAO;
    protected final ModelService modelService;
    protected final ExecutionEnvironmentManager executionEnvironmentManager;
    protected final PolicyDAO policyDAO;
    protected final ModelDAO modelDAO;
    protected ExecutionEnvironment executionEnvironment;
    private final DSLContext ctx;

    public TrainingService(ExecutionProvider executionProvider,
                           RunDAO runDAO, ModelService modelService,
                           ExecutionEnvironmentManager executionEnvironmentManager,
                           PolicyDAO policyDAO,
                           ModelDAO modelDAO,
                           DSLContext ctx) {
        this.executionProvider = executionProvider;
        this.runDAO = runDAO;
        this.modelService = modelService;
        this.executionEnvironmentManager = executionEnvironmentManager;
        this.policyDAO = policyDAO;
        this.modelDAO = modelDAO;
        this.ctx = ctx;

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
    	    // set the current Execution Environment for the given user
            long userId = this.modelDAO.getUserForModel(exp.getModelId());
            this.executionEnvironment = this.executionEnvironmentManager.getEnvironment(userId);

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