package io.skymind.pathmind.db.dao;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ExecutionProviderMetaDataDAO
{

    public enum ExecutionProviderClass {
        // These numeric ids are stored to the database so if changed, database migrations are needed
        Rescale(1);

        private final int id;

        ExecutionProviderClass(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public enum IdType {
        // These numeric ids are stored to the database so if changed, database migrations are needed
        ModelFile(0),
        Run(1),
        CheckPointFile(2);

        private final int id;

        IdType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    private final DSLContext ctx;

    public ExecutionProviderMetaDataDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void putRescaleRunJobId(long runId, String value) {
        put(ExecutionProviderClass.Rescale, IdType.Run, runId, value);
    }

    public Map<Long, String> getRescaleRunJobIds(List<Long> runIds) {
        return get(IdType.Run, runIds);
    }

    // STEPH -> REFACTOR -> Was never called before. It was a service of a service but it was never ultimately called in the code.
    public void deleteRescaleRunJobId(long runId) {
        delete(IdType.Run, runId);
    }

    public void putModelFileKey(long modelId, String value) {
        put(ExecutionProviderClass.Rescale, IdType.ModelFile, modelId, value);
    }

    public String getModelFileKey(long modelId) {
        return get(IdType.ModelFile, modelId);
    }

    // STEPH -> REFACTOR -> Was never called before. It was a service of a service but it was never ultimately called in the code.
    public void deleteModelFileKey(long modelId) {
        delete(IdType.Run, modelId);
    }

    public void putCheckPointFileKey(String policyExternalId, Object value) {
//        put(ExecutionProviderClass.Rescale, IdType.CheckPointFile, policyExternalId, value);
    }

    public String getCheckPointFileKey(String policyExternalId) {
//        return get(ExecutionProviderClass.Rescale, IdType.CheckPointFile, policyExternalId);
        return null;
    }

    private void put(ExecutionProviderClass providerClass, IdType type, long key, String value) {
        ExecutionProviderMetaDataRepository.put(ctx, providerClass.getId(), type.getId(), key, value);
    }

    private String get(IdType type, long key) {
        return ExecutionProviderMetaDataRepository.get(ctx, type.getId(), key);
    }

    private Map<Long, String> get(IdType type, List<Long> keys) {
        return ExecutionProviderMetaDataRepository.get(ctx, type.getId(), keys);
    }

    private void delete(IdType type, long key) {
        ExecutionProviderMetaDataRepository.delete(ctx, type.getId(), key);
    }
}
