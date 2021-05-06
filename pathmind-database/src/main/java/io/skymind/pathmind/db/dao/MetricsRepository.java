package io.skymind.pathmind.db.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.skymind.pathmind.db.jooq.tables.Experiment;
import io.skymind.pathmind.db.utils.JooqUtils;
import io.skymind.pathmind.shared.data.Metrics;
import org.jooq.DSLContext;
import org.jooq.Query;

import static io.skymind.pathmind.db.jooq.Tables.EXPERIMENT;
import static io.skymind.pathmind.db.jooq.Tables.METRICS;
import static io.skymind.pathmind.db.jooq.Tables.MODEL;
import static io.skymind.pathmind.db.jooq.tables.Run.RUN;
import static org.jooq.impl.DSL.max;

class MetricsRepository {

    protected static Map<Long, Integer> getMaxMetricsIterationForPolicies(DSLContext ctx, List<Long> policyIds) {
        return ctx.select(METRICS.POLICY_ID, max(METRICS.ITERATION))
                .from(METRICS)
                .where(METRICS.POLICY_ID.in(policyIds))
                .groupBy(METRICS.POLICY_ID)
                .fetchMap(METRICS.POLICY_ID, max(METRICS.ITERATION));
    }

    protected static Map<Long, List<Metrics>> getMetricsForPolicies(DSLContext ctx, List<Long> policyIds) {
        return ctx.select(METRICS.asterisk())
                .from(METRICS)
                .where(METRICS.POLICY_ID.in(policyIds))
                .orderBy(METRICS.POLICY_ID, METRICS.AGENT, METRICS.ITERATION, METRICS.INDEX)
                .fetchGroups(METRICS.POLICY_ID, record -> new Metrics(
                        record.get(METRICS.AGENT),
                        record.get(METRICS.ITERATION),
                        record.get(METRICS.INDEX),
                        JooqUtils.getSafeDouble(record.get(METRICS.MAX)),
                        JooqUtils.getSafeDouble(record.get(METRICS.MIN)),
                        JooqUtils.getSafeDouble(record.get(METRICS.MEAN)))
                );
    }

    /**
     * Instead of calling getMetricsForPolicy() with a single policy I've re-written the method as it's faster with JOOQ.
     */
    protected static List<Metrics> getMetricsForPolicy(DSLContext ctx, long policyId) {
        return ctx.select(METRICS.asterisk())
                .from(METRICS)
                .where(METRICS.POLICY_ID.eq(policyId))
                .orderBy(METRICS.POLICY_ID, METRICS.AGENT, METRICS.ITERATION, METRICS.INDEX)
                .fetch(record -> new Metrics(
                        record.get(METRICS.AGENT),
                        record.get(METRICS.ITERATION),
                        record.get(METRICS.INDEX),
                        JooqUtils.getSafeDouble(record.get(METRICS.MEAN)))
                );
    }

    /**
     * This is due to a limitation in JOOQ which does not allow dynamic multi-row inserts https://github.com/jOOQ/jOOQ/issues/6604
     * we have to rely on batching.
     */
    protected static void insertMetrics(DSLContext ctx, Map<Long, List<Metrics>> metricsByPolicyId) {
        List<Query> insertQueries = new ArrayList<>();
        metricsByPolicyId.forEach((policyId, metricsList) ->
                metricsList.stream().forEach(metrics -> {
                    insertQueries.add(
                            ctx.insertInto(METRICS)
                                    .columns(METRICS.INDEX, METRICS.MAX, METRICS.MIN, METRICS.MEAN, METRICS.ITERATION, METRICS.AGENT, METRICS.POLICY_ID)
                                    .values(metrics.getIndex(),
                                            JooqUtils.getSafeBigDecimal(metrics.getMax()),
                                            JooqUtils.getSafeBigDecimal(metrics.getMin()),
                                            JooqUtils.getSafeBigDecimal(metrics.getMean()),
                                            metrics.getIteration(),
                                            metrics.getAgent(),
                                            policyId)
                    );
                })
        );
        ctx.batch(insertQueries).execute();
    }

    protected static int getRewardNumForRun(DSLContext ctx, long runId) {
        return ctx.select(MODEL.REWARD_VARIABLES_COUNT)
                .from(RUN)
                .leftJoin(Experiment.EXPERIMENT).on(Experiment.EXPERIMENT.ID.eq(RUN.EXPERIMENT_ID))
                .leftJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
                .where(RUN.ID.eq(runId))
                .fetchOne(MODEL.REWARD_VARIABLES_COUNT);
    }

    protected static int getAgentsNumForRun(DSLContext ctx, long runId) {
        return ctx.select(MODEL.NUMBER_OF_AGENTS)
                .from(RUN)
                .leftJoin(Experiment.EXPERIMENT).on(Experiment.EXPERIMENT.ID.eq(RUN.EXPERIMENT_ID))
                .leftJoin(MODEL).on(MODEL.ID.eq(EXPERIMENT.MODEL_ID))
                .where(RUN.ID.eq(runId))
                .fetchOne(MODEL.NUMBER_OF_AGENTS);
    }

}
