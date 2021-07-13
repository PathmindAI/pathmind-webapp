package io.skymind.pathmind.db.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import io.skymind.pathmind.db.utils.JooqUtils;
import io.skymind.pathmind.shared.data.MetricsRaw;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.DSLContext;
import org.jooq.Query;

import static io.skymind.pathmind.db.jooq.Tables.METRICS;
import static io.skymind.pathmind.db.jooq.Tables.METRICS_RAW;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.max;

@Slf4j
public class MetricsRawRepository {

    protected static Map<Long, Integer> getMaxMetricsRawIterationForPolicies(DSLContext ctx, List<Long> policyIds) {
        log.trace("getMaxMetricsRawIterationForPolicies {}", policyIds);
        return ctx.select(METRICS_RAW.POLICY_ID, max(METRICS_RAW.ITERATION))
            .from(METRICS_RAW)
            .where(METRICS_RAW.POLICY_ID.in(policyIds))
            .groupBy(METRICS_RAW.POLICY_ID)
            .fetchMap(METRICS_RAW.POLICY_ID, max(METRICS_RAW.ITERATION));
    }

    public static List<MetricsRaw> getMetricsRawForPolicy(DSLContext ctx, long policyId) {
        log.trace("getMetricsRawForPolicy {}", policyId);
        return ctx.select(METRICS_RAW.asterisk())
                .from(METRICS_RAW)
                .where(METRICS_RAW.POLICY_ID.eq(policyId))
                .orderBy(METRICS_RAW.POLICY_ID, METRICS_RAW.AGENT, METRICS_RAW.ITERATION, METRICS_RAW.EPISODE, METRICS_RAW.INDEX)
                .fetch(record -> new MetricsRaw(
                        record.get(METRICS_RAW.AGENT),
                        record.get(METRICS_RAW.ITERATION),
                        record.get(METRICS_RAW.EPISODE),
                        record.get(METRICS_RAW.INDEX),
                        JooqUtils.getSafeDouble(record.get(METRICS_RAW.VALUE)))
                );
    }

    /**
     * This is due to a limitation in JOOQ which does not allow dynamic multi-row inserts https://github.com/jOOQ/jOOQ/issues/6604
     * we have to rely on batching.
     */
    protected static void insertMetricsRaw(DSLContext ctx, Map<Long, List<MetricsRaw>> metricsRawByPolicyId) {
        log.trace("insertMetricsRaw");
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


    public static Map<Long,List<MetricsRaw>> getMetricsRawForPolicies(DSLContext ctx, List<Long> policyIds) {
        log.trace("getMetricsRawForPolicies {}", policyIds);
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

    /**
     select distinct on (policy_id, index)
     policy_id, index, min, mean, max, iteration, agent
     from metrics M
     where M.policy_id = ?
     order by policy_id, index, iteration desc;
     */
    public static List<Double> getLastIterationMetricsMeanForPolicy(DSLContext ctx, long id) {
        return ctx.select(METRICS.POLICY_ID, METRICS.INDEX, METRICS.MIN, METRICS.MEAN, METRICS.MAX, METRICS.ITERATION, METRICS.AGENT)
                .distinctOn(METRICS.POLICY_ID, METRICS.INDEX)
                .from(METRICS)
                .where(METRICS.POLICY_ID.eq(id))
                .orderBy(METRICS.POLICY_ID, METRICS.INDEX, METRICS.ITERATION.desc())
                .fetchStream()
                .filter(Objects::nonNull)
                .map(record -> record.get(METRICS.MEAN, Double.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    private static final String AVG_FIELD = "avg";
    private static final String VARIANCE_FIELD = "variance";

    /**
     select policy_id, index, avg(value) avg, variance(value) variance
     from metrics_raw
     where policy_id = ?
     group by policy_id, index
     order by index;
     */
    public static List<Pair<Double, Double>> getMetricsRawForPolicyOptimized(DSLContext ctx, long id)  {
        return ctx.select(METRICS_RAW.POLICY_ID, METRICS_RAW.INDEX, avg(METRICS_RAW.VALUE).as(AVG_FIELD), field("variance(value)").as(VARIANCE_FIELD))
                .from(METRICS_RAW)
                .where(METRICS_RAW.POLICY_ID.eq(id))
                .groupBy(METRICS_RAW.POLICY_ID, METRICS_RAW.INDEX)
                .orderBy(METRICS_RAW.INDEX)
                .fetchStream()
                .filter(Objects::nonNull)
                .map(record -> Pair.of(record.get(AVG_FIELD, Double.class),record.get(VARIANCE_FIELD, Double.class)))
                .filter(pair -> ObjectUtils.allNotNull(pair.getLeft(), pair.getRight()))
                .collect(Collectors.toList());
    }

}
