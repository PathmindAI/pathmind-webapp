
package io.skymind.pathmind.services.training.db.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.data.PolicyUpdateBusEvent;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.services.training.progress.Progress;
import io.skymind.pathmind.services.training.progress.RewardScore;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.UnicastProcessor;

import java.util.List;
import java.util.stream.Collectors;

import static io.skymind.pathmind.data.db.Tables.POLICY;
import static io.skymind.pathmind.data.db.Tables.RUN;

@Service
public class RunUpdateServiceImpl implements RunUpdateService {
    private final DSLContext ctx;
    private final ObjectMapper mapper;
    private final UnicastProcessor<PathmindBusEvent> publisher;

    public RunUpdateServiceImpl(DSLContext ctx, ObjectMapper mapper, UnicastProcessor<PathmindBusEvent> publisher){
        this.ctx = ctx;
        this.mapper = mapper;
        this.publisher = publisher;
    }

    @Override
    public List<Long> getExecutingRuns() {
        return ctx.select(RUN.ID).from(RUN).where(RUN.STATUS.eq(RunStatus.Starting.getValue()).or(RUN.STATUS.eq(RunStatus.Running.getValue()))).fetch(RUN.ID);
    }

    @Override
    @Transactional
    public void updateRun(long runId, RunStatus status, List<Progress> progresses) {
        ctx.update(RUN)
                .set(RUN.STATUS, status.getValue())
                .where(RUN.ID.eq(runId))
                .execute();

        for (Progress progress : progresses) {
            try {
                final JSONB serialized = JSONB.valueOf(mapper.writeValueAsString(progress));

                ctx.insertInto(POLICY)
                        .columns(POLICY.RUN_ID, POLICY.EXTERNAL_ID, POLICY.PROGRESS)
                        .values(runId, progress.getId(), serialized)
                        .onDuplicateKeyUpdate()
                        .set(POLICY.PROGRESS, serialized)
                        .execute();

                final Policy policy = new Policy();
                policy.setRunId(runId);
                policy.setExternalId(progress.getId());
                policy.getScores().addAll(progress.getRewardProgression().stream().map(RewardScore::getMean).collect(Collectors.toList()));
                publisher.onNext(new PolicyUpdateBusEvent(policy));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
