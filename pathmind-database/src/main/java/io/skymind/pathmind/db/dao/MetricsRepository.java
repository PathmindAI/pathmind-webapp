package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.db.jooq.tables.Experiment;
import io.skymind.pathmind.db.utils.JooqUtils;
import io.skymind.pathmind.shared.data.Metrics;
import io.skymind.pathmind.shared.data.MetricsThisIter;
import org.jooq.DSLContext;
import org.jooq.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.skymind.pathmind.db.jooq.Tables.*;
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
        //todo need to change this query
        return policyIds.stream()
            .map(policyId -> {
                List<Metrics> metrics = getMetricsForPolicy(ctx, policyId);
                return Map.entry(policyId, metrics);
            })
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    protected static List<Metrics> getMetricsForPolicy(DSLContext ctx, long policyId) {
        Map<Integer, List<MetricsThisIter>> metricsMap = ctx.select(METRICS.INDEX, METRICS.MAX, METRICS.MIN, METRICS.MEAN, METRICS.ITERATION)
            .from(METRICS)
            .where(METRICS.POLICY_ID.eq(policyId))
            .orderBy(METRICS.ITERATION)
            .fetchGroups(METRICS.ITERATION, record -> new MetricsThisIter(
                record.get(METRICS.INDEX),
                JooqUtils.getSafeDouble(record.get(METRICS.MAX)),
                JooqUtils.getSafeDouble(record.get(METRICS.MIN)),
                JooqUtils.getSafeDouble(record.get(METRICS.MEAN)
            )));


        return metricsMap.entrySet().stream()
            .map(e -> new Metrics(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
    }

    /**
     * This is due to a limitation in JOOQ which does not allow dynamic multi-row inserts https://github.com/jOOQ/jOOQ/issues/6604
     * we have to rely on batching.
     */
    protected static void insertMetrics(DSLContext ctx, Map<Long, List<Metrics>> metricsByPolicyId) {
        List<Query> insertQueries = new ArrayList<>();
        metricsByPolicyId.forEach((policyId, metricsList) ->
                metricsList.stream().forEach(metrics -> {
                    int currentIter = metrics.getIteration();
                    metrics.getMetricsThisIter().forEach(m ->
                        insertQueries.add(
                            ctx.insertInto(METRICS)
                                .columns(METRICS.INDEX, METRICS.MAX, METRICS.MIN, METRICS.MEAN, METRICS.ITERATION, METRICS.POLICY_ID)
                                .values(m.getIndex(),
                                    JooqUtils.getSafeBigDecimal(m.getMax()),
                                    JooqUtils.getSafeBigDecimal(m.getMin()),
                                    JooqUtils.getSafeBigDecimal(m.getMean()),
                                    currentIter,
                                    policyId)
                        )
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

}
