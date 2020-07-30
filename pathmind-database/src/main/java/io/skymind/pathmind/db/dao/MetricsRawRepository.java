package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.db.utils.JooqUtils;
import io.skymind.pathmind.shared.data.MetricsRaw;
import io.skymind.pathmind.shared.data.MetricsRawThisEpisode;
import org.jooq.DSLContext;
import org.jooq.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.skymind.pathmind.db.jooq.Tables.*;
import static org.jooq.impl.DSL.max;

public class MetricsRawRepository {

    protected static Map<Long, Integer> getMaxMetricsRawIterationForPolicies(DSLContext ctx, List<Long> policyIds) {
        return ctx.select(METRICS_RAW.POLICY_ID, max(METRICS_RAW.ITERATION))
            .from(METRICS_RAW)
            .where(METRICS_RAW.POLICY_ID.in(policyIds))
            .groupBy(METRICS_RAW.POLICY_ID)
            .fetchMap(METRICS_RAW.POLICY_ID, max(METRICS_RAW.ITERATION));
    }

    /**
     * This is due to a limitation in JOOQ which does not allow dynamic multi-row inserts https://github.com/jOOQ/jOOQ/issues/6604
     * we have to rely on batching.
     */
    protected static void insertMetricsRaw(DSLContext ctx, Map<Long,List<MetricsRaw>> metricsRawByPolicyId) {
        List<Query> insertQueries = new ArrayList<>();
        metricsRawByPolicyId.forEach((policyId, metricsRawList) ->
                metricsRawList.stream().forEach(metricsRaw -> {
                    int currentIter = metricsRaw.getIteration();
                    for (int episode = 0; episode < metricsRaw.getEpisodeRaw().size(); episode++) {
                        List<MetricsRawThisEpisode> metricsRawData = metricsRaw.getEpisodeRaw().get(episode);
                        int episodeNum = episode;
                        metricsRawData.stream().forEach(raw ->
                            insertQueries.add(
                                ctx.insertInto(METRICS_RAW)
                                    .columns(METRICS_RAW.INDEX, METRICS_RAW.VALUE, METRICS_RAW.EPISODE, METRICS_RAW.ITERATION, METRICS_RAW.POLICY_ID)
                                    .values(raw.getIndex(),
                                        JooqUtils.getSafeBigDecimal(raw.getValue()),
                                        episodeNum,
                                        currentIter,
                                        policyId
                                        )
                            )
                        );
                    }
                })
        );
        ctx.batch(insertQueries).execute();
    }
}
