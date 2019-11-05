
package io.skymind.pathmind.services.training.db.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.data.PolicyUpdateBusEvent;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.*;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.db.repositories.RunRepository;
import io.skymind.pathmind.services.training.progress.Progress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.UnicastProcessor;

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
    private final UnicastProcessor<PathmindBusEvent> publisher;

    private final static String lrPatternStr = "lr=.*,";
    private final static Pattern lrPattern = Pattern.compile(lrPatternStr);

    public RunUpdateServiceImpl(DSLContext ctx, ObjectMapper mapper, UnicastProcessor<PathmindBusEvent> publisher) {
        this.ctx = ctx;
        this.mapper = mapper;
        this.publisher = publisher;
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
        Experiment experiment = run.getExperiment();
        Model model = run.getModel();
        Project project = run.getProject();

        ctx.update(EXPERIMENT)
                .set(EXPERIMENT.LAST_ACTIVITY_DATE, LocalDateTime.now())
                .where(EXPERIMENT.ID.eq(experiment.getId()))
                .execute();

        if (progresses.size() > 0) {
            Progress progress = progresses.get(0);

            // original name ex: PPO_PathmindEnvironment_0_gamma=0.99,lr=1e-05,sgd_minibatch_size=128_2019-10-11_21-16-2858waz_89
            // get rid of time and extra info
            // add run type and "TEMP"
            String policyTempName = progress.getId().substring(0, progress.getId().length() - 27) + run.getRunType() + "TEMP";

            Matcher matcher = lrPattern.matcher(policyTempName);

            if (matcher.find()) {
                String lr = matcher.group();

                lr = lr.replace("lr=", "").replace(",", "");
                lr = "lr=" + Double.valueOf(lr).toString() + ",";

                policyTempName = policyTempName.replaceFirst(lrPatternStr, lr);
            }

            //PPO_PathmindEnvironment_0_gamma=0.99,lr=1e-05,sgd_minibatch_size=128_1TEMP
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
                policy.setProgress(progressJsonStr);
                policy.setExperiment(experiment);
                policy.setModel(model);
                policy.setProject(project);

                // For performance reasons.
                policy.setStartedAt(progress.getStartedAt());
                policy.setAlgorithm(progress.getAlgorithm());
                policy.setStoppedAt(progress.getStoppedAt());

                // For performance reasons.
                policy.setParsedName(PolicyUtils.parsePolicyName(policy.getName()));
                policy.setNotes(PolicyUtils.getNotesFromName(policy));

                publisher.onNext(new PolicyUpdateBusEvent(policy));

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
    public List<Policy> getStoppedPolicies(List<Long> runIds) {
        return ctx.selectFrom(POLICY)
                .where(POLICY.RUN_ID.in(runIds))
                .and(POLICY.STOPPEDAT.isNotNull())
                .fetchInto(Policy.class);
    }
}
