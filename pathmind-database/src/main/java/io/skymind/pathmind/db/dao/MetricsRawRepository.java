package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.db.utils.JooqUtils;
import io.skymind.pathmind.shared.data.MetricsRaw;
import io.skymind.pathmind.shared.data.MetricsRawThisEpisode;
import io.skymind.pathmind.shared.utils.MetricsRawUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jooq.DSLContext;
import org.jooq.Query;

import java.util.*;

import static io.skymind.pathmind.db.jooq.Tables.METRICS;
import static io.skymind.pathmind.db.jooq.Tables.METRICS_RAW;
import static java.util.stream.Collectors.groupingBy;
import static org.jooq.impl.DSL.max;

public class MetricsRawRepository {

    @AllArgsConstructor
    @Data
    static class MetricsRawTemp {
        private int iteration;
        private int episode;
        private int index;
        private double value;
    }


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
        MetricsRawUtils.toMetricsRawThisEpisodeList(metricsRawByPolicyId).stream()
            .forEach(metricsRawFlat ->
                insertQueries.add(
                    ctx.insertInto(METRICS_RAW)
                        .columns(METRICS_RAW.INDEX, METRICS_RAW.VALUE, METRICS_RAW.EPISODE, METRICS_RAW.ITERATION, METRICS_RAW.POLICY_ID)
                        .values(metricsRawFlat.getIndex(),
                        JooqUtils.getSafeBigDecimal(metricsRawFlat.getValue()),
                        metricsRawFlat.getEpisode(),
                        metricsRawFlat.getIteration(),
                        metricsRawFlat.getPolicyId()
                    )
            )
        );
        ctx.batch(insertQueries).execute();
    }


    // todo : clean up
    public static Map<Long,List<MetricsRaw>> getMetricsRawForPolicies(DSLContext ctx, List<Long> policyIds) {
        Map<Long, List<MetricsRawTemp>> subresult = ctx.select(METRICS_RAW.POLICY_ID, METRICS_RAW.ITERATION, METRICS_RAW.EPISODE, METRICS_RAW.INDEX, METRICS_RAW.VALUE)
            .from(METRICS_RAW)
            .where(METRICS_RAW.POLICY_ID.in(policyIds))
            .orderBy(METRICS_RAW.POLICY_ID, METRICS_RAW.ITERATION, METRICS_RAW.EPISODE, METRICS_RAW.INDEX)
            .fetchGroups(METRICS.POLICY_ID, record ->
                new MetricsRawTemp(
                    record.get(METRICS_RAW.ITERATION),
                    record.get(METRICS_RAW.EPISODE),
                    record.get(METRICS_RAW.INDEX),
                    JooqUtils.getSafeDouble(record.get(METRICS_RAW.VALUE))
                    )
            );

        Map<Long,List<MetricsRaw>> result = new HashMap<>();

        subresult.entrySet().stream().forEach(e -> {
            long policyId = e.getKey();
            // (iteration | (episode | (index | value)))
            final Map<Integer, Map<Integer, Map<Integer, List<MetricsRawTemp>>>> collect = e.getValue().stream()
                .collect(groupingBy(MetricsRawTemp::getIteration, groupingBy(MetricsRawTemp::getEpisode, groupingBy(MetricsRawTemp::getIndex))));

            List<MetricsRaw> metricsRaw = new ArrayList<>();
            collect.entrySet().stream()
                .forEach(iterAndRaw -> {
                    int iteration = iterAndRaw.getKey();
                    List<List<MetricsRawThisEpisode>> episodeRaw = new ArrayList<>();
                    iterAndRaw.getValue().entrySet().stream()
                        .forEach(episodeAndRaw -> {
                            List<MetricsRawThisEpisode> indexRaw = new ArrayList<>();
                            episodeAndRaw.getValue().entrySet().stream()
                                .forEach(indexAndRaw -> {
                                    int index = indexAndRaw.getKey();
                                    double value = indexAndRaw.getValue().get(0).getValue();
                                    indexRaw.add(new MetricsRawThisEpisode(index, value));
                                });
                            episodeRaw.add(indexRaw);
                        });
                    metricsRaw.add(new MetricsRaw(iteration, episodeRaw));
                });
            result.put(policyId, metricsRaw);
        });

        return result;
    }
}
