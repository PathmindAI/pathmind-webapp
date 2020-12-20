package io.skymind.pathmind.services;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.shared.constants.RunStatus;
import io.skymind.pathmind.shared.data.Policy;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.skymind.pathmind.db.jooq.tables.Experiment.EXPERIMENT;

@Slf4j
@Service
public class ExperimentGoalsUpdateAsyncBatchService {

    private final RunDAO runDAO;
    private final ExperimentDAO experimentDAO;
    private final DSLContext ctx;

    private final Boolean skip;

    public ExperimentGoalsUpdateAsyncBatchService(@Value("${pathmind.skip-goals-migration}") boolean skip,
                                                  RunDAO runDAO, ExperimentDAO experimentDAO, DSLContext ctx) {
        this.runDAO = runDAO;
        this.experimentDAO = experimentDAO;
        this.ctx = ctx;
        this.skip = skip;
    }

    @Async
    public void migrateExperimentGoalsCalculation() {
        if (skip) {
            log.warn("Skipping goals migration");
            return;
        }
        log.info("Experiments goals migration started");
        List<Integer> batch;
        do {
            batch = getNextBatch(100);
            log.info("Updating goals for next batch of experiments");
            for (Integer experimentId : batch) {
                try {
                    ctx.transaction(conf -> {
                        DSLContext transactionCtx = DSL.using(conf);
                        experimentDAO.getFullExperiment(experimentId).ifPresent(experiment -> {
                            runDAO.calculateGoals(transactionCtx, experiment);
                        });
                    });
                } catch (Throwable err) {
                    log.error("Failed to update experiment {}", experimentId, err);
                }
            }
        } while (!batch.isEmpty());
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