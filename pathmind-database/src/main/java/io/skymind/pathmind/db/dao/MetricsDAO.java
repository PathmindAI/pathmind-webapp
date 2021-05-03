package io.skymind.pathmind.db.dao;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class MetricsDAO {

    private final DSLContext ctx;
    private final String metricsLastIterSql;
    private final String rawMetricsAvgVariance;

    public MetricsDAO(DSLContext ctx,
                      @Value("classpath:sql/last-iteration-metrics.sql") Resource metricsLastIteration,
                      @Value("classpath:sql/raw_metrics-avg-variance.sql") Resource rawMetricsAvgVariance) throws IOException {
        this.ctx = ctx;
        this.metricsLastIterSql = FileUtils.readFileToString(metricsLastIteration.getFile(), Charset.defaultCharset());
        this.rawMetricsAvgVariance = FileUtils.readFileToString(rawMetricsAvgVariance.getFile(), Charset.defaultCharset());
    }

    public List<Double> getLastIterationMetricsMeanForPolicy(long id) {
        return ctx.fetchStream(metricsLastIterSql, id)
                .map(record -> record.get("mean", Double.class))
                .collect(Collectors.toList());
    }

    public List<Pair<Double, Double>> getMetricsRawForPolicy(long id)  {
        return ctx.fetchStream(rawMetricsAvgVariance, id)
                .map(record -> Pair.of(
                        record.get("avg", Double.class),
                        record.get("variance", Double.class)
                        )
                )
                .collect(Collectors.toList());
    }
}
