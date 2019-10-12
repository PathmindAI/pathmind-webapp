package io.skymind.pathmind.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.db.Tables;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.services.training.ExecutionEnvironment;
import io.skymind.pathmind.services.training.ExecutionProvider;
import io.skymind.pathmind.services.training.JobSpec;
import io.skymind.pathmind.services.training.progress.Progress;
import io.skymind.pathmind.services.training.versions.AnyLogic;
import io.skymind.pathmind.services.training.versions.PathmindHelper;
import io.skymind.pathmind.services.training.versions.RLLib;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class TrainingService {
    private static final Logger log = LoggerFactory.getLogger(TrainingService.class);
    private final ExecutionProvider executionProvider;
    private final RunDAO runDAO;
    private final ModelDAO modelDAO;
    private final PolicyDAO policyDAO;
    private final DSLContext ctx;
    private final ObjectMapper objectMapper;
    private ExecutionEnvironment executionEnvironment;

    // TODO: Move direct db access into a DAO.
    public TrainingService(ExecutionProvider executionProvider, RunDAO runDAO, ModelDAO modelDAO, PolicyDAO policyDAO, DSLContext ctx, ObjectMapper objectMapper) {
        this.executionProvider = executionProvider;
        this.runDAO = runDAO;
        this.modelDAO = modelDAO;
        this.policyDAO = policyDAO;
        this.ctx = ctx;
        this.objectMapper = objectMapper;

        executionEnvironment = new ExecutionEnvironment(AnyLogic.VERSION_8_5, PathmindHelper.VERSION_0_0_24, RLLib.VERSION_0_7_0);
    }

    public void startTestRun(Experiment exp){
        final Run run = runDAO.createRun(exp, RunType.TestRun);
        // Get model from the database, as the one we can get from the experiment doesn't have all fields
        final Model model = modelDAO.getModel(exp.getModelId());

        final JobSpec spec = new JobSpec(
                exp.getProject().getPathmindUserId(),
                model.getId(),
                exp.getId(),
                run.getId(),
                "", // not collected via UI yet
                "",    // not collected via UI yet
                exp.getRewardFunction(),
                model.getNumberOfPossibleActions(),
                model.getNumberOfObservations(),
                50, // Max 50 iterations for a test run
                executionEnvironment,
                RunType.TestRun,
                () ->modelDAO.getModelFile(model.getId()),
                Arrays.asList(1e-5),
                Arrays.asList(0.99),
                Arrays.asList(128),
                15 * 60        // 15 mins
        );

        final String executionId = executionProvider.execute(spec);

        runDAO.markAsStarting(run.getId());
        log.info("Started TEST training job with id {}", executionId);

        addTempPolicy(spec, run);
    }

    public void startDiscoveryRun(Experiment exp){
        final Run run = runDAO.createRun(exp, RunType.DiscoveryRun);
        // Get model from the database, as the one we can get from the experiment doesn't have all fields
        final Model model = modelDAO.getModel(exp.getModelId());

        final JobSpec spec = new JobSpec(
                exp.getProject().getPathmindUserId(),
                model.getId(),
                exp.getId(),
                run.getId(),
                "", // not collected via UI yet
                "",    // not collected via UI yet
                exp.getRewardFunction(),
                model.getNumberOfPossibleActions(),
                model.getNumberOfObservations(),
                100, // Max 100 iterations for a test run
                executionEnvironment,
                RunType.DiscoveryRun,
                () ->modelDAO.getModelFile(model.getId()),
                Arrays.asList(1e-3, 1e-4, 1e-5),
                Arrays.asList(0.9, 0.99),
                Arrays.asList(64, 128),
                30 * 60        // 30 mins
        );

        final String executionId = executionProvider.execute(spec);

        runDAO.markAsStarting(run.getId());
        log.info("Started DISCOVERY training job with id {}", executionId);

        addTempPolicy(spec, run);
    }

    public void startFullRun(Experiment exp, Policy pol){
        final Run run = runDAO.createRun(exp, RunType.FullRun);
        // Get model from the database, as the one we can get from the experiment doesn't have all fields
        final Model model = modelDAO.getModel(exp.getModelId());

        final JSONB progress = ctx.select(Tables.POLICY.PROGRESS).from(Tables.POLICY).where(Tables.POLICY.ID.eq(pol.getId())).fetchOne().get(Tables.POLICY.PROGRESS);
        try {
            final Progress value = objectMapper.readValue(progress.toString(), Progress.class);
            //TODO: make this work across algorithms
            final Double learningRate = Double.valueOf(value.getHyperParameters().get("lr"));
            final Double gamma = Double.valueOf(value.getHyperParameters().get("gamma"));
            final Integer batchSize = Integer.valueOf(value.getHyperParameters().get("sgd_minibatch_size"));

            final JobSpec spec = new JobSpec(
                    exp.getProject().getPathmindUserId(),
                    model.getId(),
                    exp.getId(),
                    run.getId(),
                    "", // not collected via UI yet
                    "",    // not collected via UI yet
                    exp.getRewardFunction(),
                    model.getNumberOfPossibleActions(),
                    model.getNumberOfObservations(),
                    500, // Max 100 iterations for a test run
                    executionEnvironment,
                    RunType.FullRun,
                    () ->modelDAO.getModelFile(model.getId()),
                    Arrays.asList(learningRate),
                    Arrays.asList(gamma),
                    Arrays.asList(batchSize),
                    -1        // no limit
            );

            final String executionId = executionProvider.execute(spec);

            runDAO.markAsStarting(run.getId());
            log.info("Started FULL training job with id {}", executionId);

            addTempPolicy(spec, run);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addTempPolicy(JobSpec spec, Run run) {
        // this is for ui filling gap until ui get a training progress from backend(rescale)
        Policy tempPolicy = new Policy();

        String name = getTempPolicyName(Algorithm.PPO.toString(),
                "PathmindEnvironment",
                spec.getLearningRates(),
                spec.getGammas(),
                spec.getBatchSizes(),
                run.getRunType());

        tempPolicy.setName(name);
        tempPolicy.setExternalId(name);
        tempPolicy.setRunId(run.getId());

        policyDAO.insertPolicy(tempPolicy);
    }

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
                runType + "TEMP"
        );

        return name;
    }
}
