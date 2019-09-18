
package io.skymind.pathmind.services.training;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.services.training.progress.Progress;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.skymind.pathmind.data.db.Tables.POLICY;
import static io.skymind.pathmind.data.db.Tables.RUN;

@Service
public class RunUpdateServiceImpl implements RunUpdateService {
    private final DSLContext ctx;
    private final ObjectMapper mapper;

    public RunUpdateServiceImpl(DSLContext ctx, ObjectMapper mapper){
        this.ctx = ctx;
        this.mapper = mapper;
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
                .execute();

        for (Progress progress : progresses) {
            try {
                final String serialized = mapper.writeValueAsString(progress);

                ctx.insertInto(POLICY)
                        .columns(POLICY.RUN_ID, POLICY.EXTERNAL_ID, POLICY.PROGRESS)
                        .values(runId, progress.getId(), serialized)
                        .onDuplicateKeyUpdate()
                        .set(POLICY.PROGRESS, serialized)
                        .execute();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
