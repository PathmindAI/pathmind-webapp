
package io.skymind.pathmind.services.training.db.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.bus.EventBus;
import io.skymind.pathmind.bus.events.PolicyUpdateBusEvent;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.*;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.db.repositories.RunRepository;
import io.skymind.pathmind.services.TrainingService;
import io.skymind.pathmind.services.training.progress.Progress;
import io.skymind.pathmind.services.training.progress.RewardScore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.skymind.pathmind.data.db.Tables.*;

@Service
public class RunUpdateServiceImpl implements RunUpdateService {
    private static Logger log = LogManager.getLogger(RunUpdateServiceImpl.class);

    private final DSLContext ctx;
    private final ObjectMapper mapper;

    private static final String lrPatternStr = "lr=.*,";
    private static final Pattern lrPattern = Pattern.compile(lrPatternStr);

    @Autowired
    private PolicyDAO policyDao;

    @Autowired
    private RunDAO runDao;

    public RunUpdateServiceImpl(DSLContext ctx, ObjectMapper mapper) {
        this.ctx = ctx;
        this.mapper = mapper;
    }

    @Override
    public List<Long> getExecutingRuns() {
        return ctx.selectDistinct(RUN.ID)
                .from(RUN)
                .leftOuterJoin(POLICY)
                .on(POLICY.RUN_ID.eq(RUN.ID))
                .where(RUN.STATUS.eq(RunStatus.Starting.getValue())
                        .or(RUN.STATUS.eq(RunStatus.Running.getValue()))
                        .or(RUN.STATUS.eq(RunStatus.Completed.getValue()))
                        .and(POLICY.FILE.isNull()))
                .fetch(RUN.ID);
    }

    @Override
    @Transactional
    public void updateRun(long runId, RunStatus status, List<Progress> progresses) {
        LocalDateTime now = LocalDateTime.now();
        ctx.update(RUN)
                .set(RUN.STATUS, status.getValue())
                .set(RUN.STOPPED_AT, RunStatus.isRunning(status) ? null : now)
                .where(RUN.ID.eq(runId))
                .execute();

        // TODO -> DH -> Can you please adjust how you would prefer to have the backend setup because it's quite janky right now. I just temporarily
        //  put this to get the solution working and avoid code duplication. I basically need the model and experiment data models.
        Run run = RunRepository.getRun(ctx, runId);
        // IMPORTANT -> Needed to prevent an issue with the training list missing data. Unfortunately the get (SELECT) returns the values
        // from before the UPDATE has been saved to the database. This is also required for the EventBus.post() to work as the training
        // list requires this information.
        run.setStatusEnum(status);
        run.setStoppedAt(RunStatus.isRunning(status) ? null : now);

        Experiment experiment = run.getExperiment();
        Model model = run.getModel();
        Project project = run.getProject();

        ctx.update(EXPERIMENT)
                .set(EXPERIMENT.LAST_ACTIVITY_DATE, LocalDateTime.now())
                .where(EXPERIMENT.ID.eq(experiment.getId()))
                .execute();

        if (policyDao.isTemporaryPolicy(runId, TrainingService.TEMPORARY_POSTFIX) && progresses.size() > 0) {
            Progress progress = progresses.get(0);

            String policyTempName = policyTempName(progress.getId(), run.getRunType());

            ctx.update(POLICY)
                    .set(POLICY.NAME, progress.getId())
                    .set(POLICY.EXTERNAL_ID, progress.getId())
                    .where(POLICY.RUN_ID.eq(runId), POLICY.EXTERNAL_ID.eq(policyTempName))
                    .execute();
        }

        for (Progress progress : progresses) {
            try {
                // PERFORMANCE -> We should store these values in the database rather than having to parse JSON all the time.
                final String progressJsonStr = mapper.writeValueAsString(progress);
                final JSONB progressJson = JSONB.valueOf(progressJsonStr);

                long policyId = ctx.insertInto(POLICY)
                        .columns(POLICY.NAME, POLICY.RUN_ID, POLICY.EXTERNAL_ID, POLICY.PROGRESS, POLICY.STARTEDAT, POLICY.STOPPEDAT, POLICY.ALGORITHM)
                        .values(progress.getId(), runId, progress.getId(), progressJson, progress.getStartedAt(), progress.getStoppedAt(), progress.getAlgorithm())
                        .onConflict(POLICY.RUN_ID, POLICY.EXTERNAL_ID)
                        .doUpdate()
                        .set(POLICY.PROGRESS, progressJson)
                        .set(POLICY.STARTEDAT, progress.getStartedAt())
                        .set(POLICY.STOPPEDAT, progress.getStoppedAt())
                        .returning(POLICY.ID)
                        .fetchOne()
                        .getValue(POLICY.ID);

                final Policy policy = new Policy();
                policy.setId(policyId);
                policy.setRunId(runId);
                policy.setRun(run);
                policy.setName(progress.getId());
                policy.setExternalId(progress.getId());
                policy.setScores(progress.getRewardProgression());
                policy.setExperiment(experiment);
                policy.setModel(model);
                policy.setProject(project);

                // For performance reasons.
                policy.setStartedAt(progress.getStartedAt());
                policy.setStoppedAt(progress.getStoppedAt());
                policy.setAlgorithm(progress.getAlgorithm());

                // For performance reasons.
                policy.setParsedName(PolicyUtils.parsePolicyName(policy.getName()));
                policy.setNotes(PolicyUtils.getNotesFromName(policy));

                EventBus.post(new PolicyUpdateBusEvent(policy));

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @Transactional
    public void savePolicyFile(long runId, String externalId, byte[] policyFile) {
        ctx.update(POLICY)
                .set(POLICY.FILE, policyFile)
                .where(POLICY.RUN_ID.eq(runId).and(POLICY.EXTERNAL_ID.eq(externalId)))
                .execute();
    }

    @Override
    public void saveCheckpointFile(long runId, String externalId, byte[] checkpointFile) {
        ctx.update(POLICY)
                .set(POLICY.SNAPSHOT, checkpointFile)
                .where(POLICY.RUN_ID.eq(runId).and(POLICY.EXTERNAL_ID.eq(externalId)))
                .execute();
    }

    @Override
    public List<Policy> getStoppedPolicies(List<Long> runIds) {
        return ctx.selectFrom(POLICY)
                .where(POLICY.RUN_ID.in(runIds))
                .and(POLICY.STOPPEDAT.isNotNull())
                .fetchInto(Policy.class);
    }

    @Override
    public void cleanUpTemporary(long runId) {
        boolean isExist = policyDao.isTemporaryPolicy(runId, TrainingService.TEMPORARY_POSTFIX);
        if (isExist) {
            policyDao.deleteTemporaryPolicy(runId, TrainingService.TEMPORARY_POSTFIX);
            log.info("Cleaned Temporary Policies in " + runId);
        }
    }

    @Override
    public List<RewardScore> getScores(long runId, String policyExtId) {
        Policy policy =  policyDao.getPolicy(runId, policyExtId);

        // check temporary policy
        if (policy == null) {
            int runType = runDao.getRun(runId).getRunType();
            policy = policyDao.getPolicy(runId, policyTempName(policyExtId, runType));
        }

        if (policy == null) {
            return null;
        }

        // todo need to get rid of json parsing after having progress db
        try {
            Progress progress = mapper.readValue(policy.getProgress(), Progress.class);
            policy.setScores(progress.getRewardProgression());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return policy.getScores();
    }

    private String policyTempName(String policyExtId, int runType) {
        // original name ex: PPO_PathmindEnvironment_0_gamma=0.99,lr=1e-05,sgd_minibatch_size=128_2019-10-11_21-16-2858waz_89
        // get rid of time and extra info
        // add run type and "TEMP"
        String policyTempName = policyExtId.substring(0, policyExtId.length() - 27) + runType + TrainingService.TEMPORARY_POSTFIX;

        Matcher matcher = lrPattern.matcher(policyTempName);

        if (matcher.find()) {
            String lr = matcher.group();

            lr = lr.replace("lr=", "").replace(",", "");
            lr = "lr=" + Double.valueOf(lr).toString() + ",";

            policyTempName = policyTempName.replaceFirst(lrPatternStr, lr);
        }

        //PPO_PathmindEnvironment_0_gamma=0.99,lr=1e-05,sgd_minibatch_size=128_1TEMP
        return policyTempName;
    }
}
