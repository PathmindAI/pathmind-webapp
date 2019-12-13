package io.skymind.pathmind.db.dao;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        put(ExecutionProviderClass.Rescale, IdType.Run, String.valueOf(runId), value);
    }

    public Map<Long, String> getRescaleRunJobIds(List<Long> runIds) {
        List<String> ids = runIds.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());

        return get(IdType.Run, ids).entrySet().stream()
                .map(e -> Map.entry(Long.valueOf(e.getKey()), e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // STEPH -> REFACTOR -> Was never called before. It was a service of a service but it was never ultimately called in the code.
    public void deleteRescaleRunJobId(long runId) {
        delete(IdType.Run, String.valueOf(runId));
    }

    public void putModelFileKey(long modelId, String value) {
        put(ExecutionProviderClass.Rescale, IdType.ModelFile, String.valueOf(modelId), value);
    }

    public String getModelFileKey(long modelId) {
        return get(IdType.ModelFile, String.valueOf(modelId));
    }

    // STEPH -> REFACTOR -> Was never called before. It was a service of a service but it was never ultimately called in the code.
    public void deleteModelFileKey(long modelId) {
        delete(IdType.Run, String.valueOf(modelId));
    }

    public void putCheckPointFileKey(String policyExternalId, String value) {
        put(ExecutionProviderClass.Rescale, IdType.CheckPointFile, policyExternalId, value);
    }

    public String getCheckPointFileKey(String policyExternalId) {
        return get(IdType.CheckPointFile, policyExternalId);
    }

    private void put(ExecutionProviderClass providerClass, IdType type, String key, String value) {
        ExecutionProviderMetaDataRepository.put(ctx, providerClass.getId(), type.getId(), key, value);
    }

    private String get(IdType type, String key) {
        return ExecutionProviderMetaDataRepository.get(ctx, type.getId(), key);
    }

    private Map<String, String> get(IdType type, List<String> keys) {
        return ExecutionProviderMetaDataRepository.get(ctx, type.getId(), keys);
    }

    private void delete(IdType type, String key) {
        ExecutionProviderMetaDataRepository.delete(ctx, type.getId(), key);
    }
}
