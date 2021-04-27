package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.data.Metrics;
import io.skymind.pathmind.shared.data.MetricsRaw;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MetricsDAO {

    private final DSLContext ctx;

    public MetricsDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public List<Metrics> getMetricsForPolicy(long id) {
        return MetricsRepository.getMetricsForPolicy(ctx, id);
    }

    public Map<Long,List<MetricsRaw>> getMetricsRawForPolicy(List<Long> ids)  {
        return MetricsRawRepository.getMetricsRawForPolicies(ctx, ids);
    }
}
