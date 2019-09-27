package io.skymind.pathmind.services;

import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.training.ExecutionEnvironment;
import io.skymind.pathmind.services.training.ExecutionProvider;
import io.skymind.pathmind.services.training.JobSpec;
import io.skymind.pathmind.services.training.versions.AnyLogic;
import io.skymind.pathmind.services.training.versions.PathmindHelper;
import io.skymind.pathmind.services.training.versions.RLLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TrainingService {
    private static final Logger log = LoggerFactory.getLogger(TrainingService.class);
    private final ExecutionProvider executionProvider;
    private final RunDAO runDAO;
    private final ModelDAO modelDAO;
    private ExecutionEnvironment executionEnvironment;

    public TrainingService(ExecutionProvider executionProvider, RunDAO runDAO, ModelDAO modelDAO) {
        this.executionProvider = executionProvider;
        this.runDAO = runDAO;
        this.modelDAO = modelDAO;
        executionEnvironment = new ExecutionEnvironment(AnyLogic.VERSION_8_5, PathmindHelper.VERSION_0_0_24, RLLib.VERSION_0_7_0);
    }

    public void startTestRun(Experiment exp){
        final Run run = runDAO.createRun(exp, RunType.TestRun);


        final JobSpec spec = new JobSpec(
                exp.getProject().getPathmindUserId(),
                exp.getModel().getId(),
                exp.getId(),
                run.getId(),
                "", // not collected via UI yet
                "",    // not collected via UI yet
                exp.getRewardFunction(),
                exp.getModel().getNumberOfPossibleActions(),
                exp.getModel().getNumberOfObservations(),
                100, // Max 100 iterations for a test run
                executionEnvironment,
                RunType.TestRun,
                () ->modelDAO.getModelFile(exp.getModel().getId())
        );

        final String executionId = executionProvider.execute(spec);

        runDAO.markAsStarting(run.getId());
        log.info("Started training job with id {}", executionId);
    }
}
