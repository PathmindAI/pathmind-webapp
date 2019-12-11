package io.skymind.pathmind.db.dao;

import org.jooq.DSLContext;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ExecutionProviderMetaDataDAO
{
    private static final int RESCALE_EXECUTION_PROVIDER_CLASS = 1;

    private static final int MODEL_FILE_ID_TYPE = 0;
    private static final int RUN_ID_TYPE = 1;

    private final DSLContext ctx;

    public ExecutionProviderMetaDataDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void putRescaleRunJobId(long runId, String value) {
        put(RESCALE_EXECUTION_PROVIDER_CLASS, RUN_ID_TYPE, runId, value);
    }

    public Map<Long, String> getRescaleRunJobIds(List<Long> runIds) {
        return get(RUN_ID_TYPE, runIds);
    }

    // STEPH -> REFACTOR -> Was never called before. It was a service of a service but it was never ultimately called in the code.
    public void deleteRescaleRunJobId(long runId) {
        delete(RUN_ID_TYPE, runId);
    }

    public void putModelFileKey(long modelId, String value) {
        put(RESCALE_EXECUTION_PROVIDER_CLASS, MODEL_FILE_ID_TYPE, modelId, value);
    }

    public String getModelFileKey(long modelId) {
        return get(MODEL_FILE_ID_TYPE, modelId);
    }

    // STEPH -> REFACTOR -> Was never called before. It was a service of a service but it was never ultimately called in the code.
    public void deleteModelFileKey(long modelId) {
        delete(MODEL_FILE_ID_TYPE, modelId);
    }

    private void put(int providerClass, int type, long key, String value) {
        ExecutionProviderMetaDataRepository.put(ctx, providerClass, type, key, value);
    }

    private String get(int type, long key) {
        return ExecutionProviderMetaDataRepository.get(ctx, type, key);
    }

    private Map<Long, String> get(int type, List<Long> keys) {
        return ExecutionProviderMetaDataRepository.get(ctx, type, keys);
    }

    private void delete(int type, long key) {
        ExecutionProviderMetaDataRepository.delete(ctx, type, key);
    }
}
