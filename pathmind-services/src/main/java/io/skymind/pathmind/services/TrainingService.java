package io.skymind.pathmind.services;

import java.util.List;
import java.util.stream.Collectors;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.db.utils.DBUtils;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Run;
import io.skymind.pathmind.shared.services.training.ExecutionProvider;
import io.skymind.pathmind.shared.services.training.environment.ExecutionEnvironment;
import io.skymind.pathmind.shared.services.training.environment.ExecutionEnvironmentManager;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import static io.skymind.pathmind.shared.constants.RunType.DiscoveryRun;

@Slf4j
public abstract class TrainingService {
    protected final ExecutionProvider executionProvider;
    protected final RunDAO runDAO;
    protected final ModelService modelService;
    protected final ExecutionEnvironmentManager executionEnvironmentManager;
    protected final PolicyDAO policyDAO;
    protected final ModelDAO modelDAO;
    protected final ExperimentDAO experimentDAO;
    protected ExecutionEnvironment executionEnvironment;
    private final DSLContext ctx;

    public TrainingService(ExecutionProvider executionProvider,
                           RunDAO runDAO, ModelService modelService,
                           ExecutionEnvironmentManager executionEnvironmentManager,
                           PolicyDAO policyDAO,
                           ModelDAO modelDAO,
                           ExperimentDAO experimentDAO,
                           DSLContext ctx) {
        this.executionProvider = executionProvider;
        this.runDAO = runDAO;
        this.modelService = modelService;
        this.executionEnvironmentManager = executionEnvironmentManager;
        this.policyDAO = policyDAO;
        this.modelDAO = modelDAO;
        this.experimentDAO = experimentDAO;
        this.ctx = ctx;

    }

    public void startRun(Experiment exp){
        // set the current Execution Environment for the given user
        long userId = this.modelDAO.getUserForModel(exp.getModelId());
        this.executionEnvironment = this.executionEnvironmentManager.getEnvironment(userId);

        startRun(exp,
            executionEnvironment.getPBT_RUN_ITERATIONS(),
            executionEnvironment.getPBT_MAX_TIME_IN_SEC(),
            executionEnvironment.getPBT_NUM_SAMPLES()
        );
    }

    private void startRun(Experiment exp, int iterations, int maxTimeInSec, int numSamples) {
    	ctx.transaction(conf -> {
            DSLContext transactionCtx = DSL.using(conf);

    		Run run = runDAO.createRun(transactionCtx, exp, DiscoveryRun);
    		exp.addRun(run);
    		run.setStatusEnum(RunStatus.Starting);
    		String executionId = startRun(exp.getModel(), exp, run, iterations, maxTimeInSec, numSamples);
    		runDAO.markAsStarting(transactionCtx, run.getId(), executionId);
    		ExperimentUtils.updateTrainingStatus(exp);
    		experimentDAO.updateTrainingStatus(transactionCtx, exp);
            log.info("Started {} training job with id {}", DiscoveryRun, executionId);
    	});
    }

    protected abstract String startRun(Model model, Experiment exp, Run run, int iterations, int maxTimeInSec, int numSampes);

    public void stopRun(Experiment experiment)  {
        ctx.transaction(conf -> {
            DSLContext transactionCtx = DSL.using(conf);

            DBUtils.setLockTimeout(transactionCtx, 4);

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
                run.setStatusEnum(RunStatus.Stopping);
                runDAO.markAsStopping(transactionCtx, run);
            });
            ExperimentUtils.updateTrainingStatus(experiment);
            experimentDAO.updateTrainingStatus(transactionCtx, experiment);
            log.info("Stopped {} training job with id {}", DiscoveryRun, runs.get(0).getJobId());
        });
    }
}