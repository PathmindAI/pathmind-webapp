package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.db.jooq.tables.Experiment;
import io.skymind.pathmind.db.utils.JooqUtils;
import io.skymind.pathmind.shared.data.Metrics;
import io.skymind.pathmind.shared.data.MetricsThisIter;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jooq.DSLContext;
import org.jooq.Query;

import java.util.*;
import java.util.stream.Collectors;

import static io.skymind.pathmind.db.jooq.Tables.*;
import static io.skymind.pathmind.db.jooq.tables.Run.RUN;
import static org.jooq.impl.DSL.max;

class MetricsRepository {

    // this is a temporary class to help MetricsRepository convert data into appropriate data types
    @AllArgsConstructor
    @Data
    static class MetricsAndIter {
        private MetricsThisIter metricsThisIter;
        private Integer iteration;
    }

    protected static Map<Long, Integer> getMaxMetricsIterationForPolicies(DSLContext ctx, List<Long> policyIds) {
        return ctx.select(METRICS.POLICY_ID, max(METRICS.ITERATION))
            .from(METRICS)
            .where(METRICS.POLICY_ID.in(policyIds))
            .groupBy(METRICS.POLICY_ID)
            .fetchMap(METRICS.POLICY_ID, max(METRICS.ITERATION));
    }

    protected static Map<Long, List<Metrics>> getMetricsForPolicies(DSLContext ctx, List<Long> policyIds) {
        Map<Long, List<MetricsAndIter>> subresult = ctx.select(METRICS.POLICY_ID, METRICS.ITERATION, METRICS.INDEX, METRICS.MAX, METRICS.MIN, METRICS.MEAN)
            .from(METRICS)
            .where(METRICS.POLICY_ID.in(policyIds))
            .orderBy(METRICS.POLICY_ID, METRICS.ITERATION, METRICS.INDEX)
            .fetchGroups(METRICS.POLICY_ID, record -> new MetricsAndIter(
                new MetricsThisIter(
                        record.get(METRICS.INDEX),
                        JooqUtils.getSafeDouble(record.get(METRICS.MAX)),
                        JooqUtils.getSafeDouble(record.get(METRICS.MIN)),
                        JooqUtils.getSafeDouble(record.get(METRICS.MEAN))),
                record.get(METRICS.ITERATION)
            ));

        Map<Long, List<Metrics>> result = new HashMap<>();

        subresult.entrySet().stream().forEach(e -> {
            Map<Integer, List<MetricsAndIter>> groupByIteration
                = e.getValue().stream().collect(Collectors.groupingBy(MetricsAndIter::getIteration));


                List<Metrics> metrics = groupByIteration.entrySet().stream()
                    .map(entry -> new Metrics(entry.getKey(), entry.getValue().stream().map(MetricsAndIter::getMetricsThisIter).collect(Collectors.toList())))
                    .sorted(Comparator.comparing(Metrics::getIteration))
                    .collect(Collectors.toList());

                result.put(e.getKey(), metrics);
        });

        return result;
    }

    protected static List<Metrics> getMetricsForPolicy(DSLContext ctx, long policyId) {
        return getMetricsForPolicies(ctx, Collections.singletonList(policyId)).getOrDefault(policyId, Collections.emptyList());
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
