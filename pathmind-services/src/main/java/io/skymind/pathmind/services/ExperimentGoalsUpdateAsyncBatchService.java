package io.skymind.pathmind.services;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Policy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.skymind.pathmind.db.jooq.tables.Experiment.EXPERIMENT;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExperimentGoalsUpdateAsyncBatchService {

    private final PolicyDAO policyDAO;
    private final RunDAO runDAO;
    private final ExperimentDAO experimentDAO;
    private final DSLContext ctx;

    @Async
    public void migrateExperimentGoalsCalculation() {
        log.info("Experiments goals migration started");
        List<Integer> butch;
        do {
            butch = getNextBatch(100);
            log.info("Updating goals for next batch of experiments");
            for(Integer experimentId: butch) {
                try {
                    ctx.transaction(conf -> {
                        DSLContext transactionCtx = DSL.using(conf);
                        experimentDAO.getExperiment(experimentId).ifPresent(experiment -> {
                            List<Policy> policies = policyDAO.getPoliciesForExperiment(transactionCtx, experiment.getId());
                            runDAO.calculateGoals(transactionCtx, experiment, policies);
                        });
                    });
                } catch (Throwable err) {
                    log.error("Failed to update experiment {}", experimentId, err);
                }
            }
        } while (!butch.isEmpty());
        log.info("Experiments goals migration complete");
    }

    public List<Integer> getNextBatch(int limit) {
        return ctx.select(EXPERIMENT.ID)
                .from(EXPERIMENT)
                .where(EXPERIMENT.HAS_GOALS.eq(true))
                .and(EXPERIMENT.TRAINING_STATUS.ne(RunStatus.NotStarted.getValue()))
                .and(EXPERIMENT.GOALS_TOTAL_NUM.eq(0))
                .orderBy(EXPERIMENT.DATE_CREATED.desc())
                .limit(limit)
                .fetch(0, Integer.class);
    }


}