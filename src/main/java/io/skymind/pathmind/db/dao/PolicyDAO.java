package io.skymind.pathmind.db.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.data.db.Tables;
import io.skymind.pathmind.db.repositories.PolicyRepository;
import io.skymind.pathmind.services.training.progress.Progress;
import io.skymind.pathmind.services.training.progress.RewardScore;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static io.skymind.pathmind.data.db.Tables.EXPERIMENT;
import static io.skymind.pathmind.data.db.Tables.RUN;

@Repository
public class PolicyDAO extends PolicyRepository
{

    private final DSLContext ctx;
    private final io.skymind.pathmind.data.db.tables.Policy tbl;
    private final ObjectMapper objectMapper;

    public PolicyDAO(DSLContext ctx, ObjectMapper objectMapper){
        this.ctx = ctx;
        this.tbl = Tables.POLICY;
        this.objectMapper = objectMapper;
    }

    public List<Policy> getPoliciesForExperiment(long experimentId){
        final List<Policy> policies = ctx.select(tbl.ID, tbl.EXTERNAL_ID, tbl.NAME, tbl.PROGRESS, tbl.RUN_ID)
                .select(EXPERIMENT.asterisk())
                .select(RUN.asterisk())
                .from(tbl)
                .join(RUN)
                .on(tbl.RUN_ID.eq(RUN.ID))
                .join(EXPERIMENT)
                .on(EXPERIMENT.ID.eq(RUN.EXPERIMENT_ID))
                .where(RUN.EXPERIMENT_ID.eq(experimentId))
                .fetch(it -> {
                    final Policy policy = new Policy();
                    policy.setExternalId(it.get(tbl.EXTERNAL_ID));
                    policy.setId(it.get(tbl.ID));
                    policy.setName(it.get(tbl.NAME));
                    policy.setRunId(it.get(tbl.RUN_ID));

                    try {
                        final JSONB progressJson = it.get(tbl.PROGRESS);
                        final Progress progress = objectMapper.readValue(progressJson.toString(), Progress.class);
                        policy.getScores().addAll(progress.getRewardProgression().stream().map(RewardScore::getMean).collect(Collectors.toList()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    final Experiment exp = it.into(EXPERIMENT).into(Experiment.class);
                    policy.setExperiment(exp);

                    final Run run = it.into(RUN).into(Run.class);
                    policy.setRun(run);

                    return policy;
                });

        return policies;
    }
}
