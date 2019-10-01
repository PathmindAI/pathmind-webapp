
package io.skymind.pathmind.services.training.db.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.data.PolicyUpdateBusEvent;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.services.training.progress.Progress;
import io.skymind.pathmind.services.training.progress.RewardScore;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.UnicastProcessor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static io.skymind.pathmind.data.db.Tables.*;

@Service
public class RunUpdateServiceImpl implements RunUpdateService {
    private final DSLContext ctx;
    private final ObjectMapper mapper;
    private final UnicastProcessor<PathmindBusEvent> publisher;

    public RunUpdateServiceImpl(DSLContext ctx, ObjectMapper mapper, UnicastProcessor<PathmindBusEvent> publisher) {
        this.ctx = ctx;
        this.mapper = mapper;
        this.publisher = publisher;
    }

    @Override
    public List<Long> getExecutingRuns() {
        return ctx.select(RUN.ID)
                .from(RUN)
                .leftOuterJoin(POLICY)
                .on(POLICY.RUN_ID.eq(RUN.ID))
                .where(RUN.STATUS.eq(RunStatus.Starting.getValue())
                        .or(RUN.STATUS.eq(RunStatus.Running.getValue()))
                        .or(RUN.STATUS.eq(RunStatus.Completed.getValue()).and(POLICY.FILE.isNull())))
                .fetch(RUN.ID);
    }

    private Experiment getExperiment(long runId) {
        return ctx.selectFrom(EXPERIMENT).where(EXPERIMENT.ID.in(DSL.select(RUN.EXPERIMENT_ID).from(RUN).where(RUN.ID.eq(runId)))).fetchOneInto(Experiment.class);
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

        Experiment experiment = getExperiment(runId);

        ctx.update(EXPERIMENT)
                .set(EXPERIMENT.LAST_ACTIVITY_DATE, LocalDateTime.now())
                .where(EXPERIMENT.ID.eq(experiment.getId()))
                .execute();

        Run run = ctx.selectFrom(RUN).where(RUN.ID.eq(runId)).fetchOneInto(Run.class);

        for (Progress progress : progresses) {
            try {
                if (!run.getRunTypeEnum().equals(RunType.DiscoverRun) && status.equals(RunStatus.Completed)) {
                    progress.setStoppedAt(now);
                }

                if (run.getRunTypeEnum().equals(RunType.DiscoverRun) && (status.equals(RunStatus.Running) || status.equals(RunStatus.Completed))) {
                    // todo: when we change discover run iteration number, we should change this too
                    // we might better set the iteration number

                    if (progress.getRewardProgression().size() == 100 && progress.getStoppedAt() == null) {
                        progress.setStoppedAt(now);
                    }
                }

                final JSONB serialized = JSONB.valueOf(mapper.writeValueAsString(progress));

                ctx.insertInto(POLICY)
                        .columns(POLICY.NAME, POLICY.RUN_ID, POLICY.EXTERNAL_ID, POLICY.PROGRESS)
                        .values(progress.getId(), runId, progress.getId(), serialized)
                        .onConflict(POLICY.RUN_ID, POLICY.EXTERNAL_ID)
                        .doUpdate()
                        .set(POLICY.PROGRESS, serialized)
                        .execute();

                final Policy policy = new Policy();
                policy.setRunId(runId);
                policy.setExternalId(progress.getId());
                policy.getScores().addAll(progress.getRewardProgression().stream().map(RewardScore::getMean).collect(Collectors.toList()));
                policy.setExperiment(experiment);
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

}
