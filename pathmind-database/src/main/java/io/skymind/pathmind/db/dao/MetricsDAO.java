package io.skymind.pathmind.db.dao;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
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
        this.metricsLastIterSql = String.join(" ", IOUtils.readLines(metricsLastIteration.getInputStream(), Charset.defaultCharset()));
        this.rawMetricsAvgVariance = String.join(" ", IOUtils.readLines(rawMetricsAvgVariance.getInputStream(), Charset.defaultCharset()));
    }

    public List<Double> getLastIterationMetricsMeanForPolicy(long id) {
        return ctx.fetchStream(metricsLastIterSql, id)
                .filter(Objects::nonNull)
                .map(record -> record.get("mean", Double.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Pair<Double, Double>> getMetricsRawForPolicy(long id)  {
        return ctx.fetchStream(rawMetricsAvgVariance, id)
                .filter(Objects::nonNull)
                .map(record -> Pair.of(
                        record.get("avg", Double.class),
                        record.get("variance", Double.class)
                        )
                )
                .filter(pair -> ObjectUtils.allNotNull(pair.getLeft(), pair.getRight()))
                .collect(Collectors.toList());
    }
}
