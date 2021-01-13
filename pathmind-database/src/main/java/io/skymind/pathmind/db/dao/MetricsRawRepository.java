package io.skymind.pathmind.db.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.skymind.pathmind.db.utils.JooqUtils;
import io.skymind.pathmind.shared.data.MetricsRaw;
import org.jooq.DSLContext;
import org.jooq.Query;

import static io.skymind.pathmind.db.jooq.Tables.METRICS;
import static io.skymind.pathmind.db.jooq.Tables.METRICS_RAW;
import static org.jooq.impl.DSL.max;

public class MetricsRawRepository {

    protected static Map<Long, Integer> getMaxMetricsRawIterationForPolicies(DSLContext ctx, List<Long> policyIds) {
        return ctx.select(METRICS_RAW.POLICY_ID, max(METRICS_RAW.ITERATION))
                .from(METRICS_RAW)
                .where(METRICS_RAW.POLICY_ID.in(policyIds))
                .groupBy(METRICS_RAW.POLICY_ID)
                .fetchMap(METRICS_RAW.POLICY_ID, max(METRICS_RAW.ITERATION));
    }

    protected static List<MetricsRaw> getMetricsRawForPolicy(DSLContext ctx, long policyId) {
        return getMetricsRawForPolicies(ctx, Collections.singletonList(policyId)).getOrDefault(policyId, Collections.emptyList());
    }

    /**
     * This is due to a limitation in JOOQ which does not allow dynamic multi-row inserts https://github.com/jOOQ/jOOQ/issues/6604
     * we have to rely on batching.
     */
    protected static void insertMetricsRaw(DSLContext ctx, Map<Long, List<MetricsRaw>> metricsRawByPolicyId) {
        List<Query> insertQueries = new ArrayList<>();
        metricsRawByPolicyId.forEach((policyId, metricsRawList) ->
                metricsRawList.stream().forEach(metricsRaw -> {
                    insertQueries.add(
                            ctx.insertInto(METRICS_RAW)
                                    .columns(METRICS_RAW.INDEX, METRICS_RAW.VALUE, METRICS_RAW.EPISODE, METRICS_RAW.ITERATION, METRICS_RAW.AGENT, METRICS_RAW.POLICY_ID)
                                    .values(metricsRaw.getIndex(),
                                            JooqUtils.getSafeBigDecimal(metricsRaw.getValue()),
                                            metricsRaw.getEpisode(),
                                            metricsRaw.getIteration(),
                                            metricsRaw.getAgent(),
                                            policyId
                                    )
                    );
                })
        );

        ctx.batch(insertQueries).execute();
    }


    public static Map<Long, List<MetricsRaw>> getMetricsRawForPolicies(DSLContext ctx, List<Long> policyIds) {
        return ctx.select(METRICS_RAW.asterisk())
                .from(METRICS_RAW)
                .where(METRICS_RAW.POLICY_ID.in(policyIds))
                .orderBy(METRICS_RAW.POLICY_ID, METRICS_RAW.AGENT, METRICS_RAW.ITERATION, METRICS_RAW.EPISODE, METRICS_RAW.INDEX)
                .fetchGroups(METRICS.POLICY_ID, record -> new MetricsRaw(
                        record.get(METRICS_RAW.AGENT),
                        record.get(METRICS_RAW.ITERATION),
                        record.get(METRICS_RAW.EPISODE),
                        record.get(METRICS_RAW.INDEX),
                        JooqUtils.getSafeDouble(record.get(METRICS_RAW.VALUE)))
                );
    }
}
