package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.services.training.ExecutionProvider;
import io.skymind.pathmind.shared.services.training.ExecutionProviderClass;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ExecutionProviderMetaDataDAO {

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
    private final ExecutionProviderClass providerClass;

    public ExecutionProviderMetaDataDAO(DSLContext ctx, ExecutionProvider provider) {
        this.ctx = ctx;
        this.providerClass = provider.executionProviderClass();
    }

    public void putProviderRunJobId(Configuration conf, long runId, String value) {
    	DSLContext transactionContext = DSL.using(conf); 
        put(transactionContext, this.providerClass, IdType.Run, String.valueOf(runId), value);
    }

    public Map<Long, String> getProviderRunJobIds(List<Long> runIds) {
        return get(this.providerClass, IdType.Run, runIds).entrySet().stream()
                .map(e -> Map.entry(Long.valueOf(e.getKey()), e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // STEPH -> REFACTOR -> Was never called before. It was a service of a service but it was never ultimately called in the code.
    public void deleteProviderRunJobId(long runId) {
        delete(this.providerClass, IdType.Run, String.valueOf(runId));
    }

    public void putModelFileKey(long modelId, String value) {
        put(ctx, this.providerClass, IdType.ModelFile, String.valueOf(modelId), value);
    }

    public String getModelFileKey(long modelId) {
        return get(this.providerClass, IdType.ModelFile, String.valueOf(modelId));
    }

    // STEPH -> REFACTOR -> Was never called before. It was a service of a service but it was never ultimately called in the code.
    public void deleteModelFileKey(long modelId) {
        delete(this.providerClass, IdType.Run, String.valueOf(modelId));
    }

    public void putCheckPointFileKey(String policyExternalId, String value) {
        put(ctx, this.providerClass, IdType.CheckPointFile, policyExternalId, value);
    }

    public String getCheckPointFileKey(String policyExternalId) {
        return get(this.providerClass, IdType.CheckPointFile, policyExternalId);
    }

    private void put(DSLContext context, ExecutionProviderClass providerClass, IdType type, String key, String value) {
        ExecutionProviderMetaDataRepository.put(context, providerClass.getId(), type.getId(), key, value);
    }

    private String get(ExecutionProviderClass providerClass, IdType type, String key) {
        return ExecutionProviderMetaDataRepository.get(ctx, providerClass.getId(), type.getId(), key);
    }

    private Map<String, String> get(ExecutionProviderClass providerClass, IdType type, Collection<?> keys) {
        if (type.equals(IdType.CheckPointFile)) {
            keys = keys.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());
        }

        return ExecutionProviderMetaDataRepository.get(ctx, providerClass.getId(), type.getId(), keys);
    }

    private void delete(ExecutionProviderClass providerClass, IdType type, String key) {
        ExecutionProviderMetaDataRepository.delete(ctx, providerClass.getId(), type.getId(), key);
    }
}
