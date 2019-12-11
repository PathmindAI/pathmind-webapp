package io.skymind.pathmind.db.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ExecutionProviderMetaDataDAO
{
    private static final String RESCALE_EXECUTION_PROVIDER = "io.skymind.pathmind.services.training.cloud.rescale.RescaleExecutionProvider";

    private final DSLContext ctx;
    private final ObjectMapper mapper;

    public ExecutionProviderMetaDataDAO(DSLContext ctx, ObjectMapper mapper) {
        this.ctx = ctx;
        this.mapper = mapper;
    }

    public void putRescaleRunJobId(long runId, Object value) {
        put(runIdKey(runId), value);
    }

    /**
     * // STEPH -> REFACTOR -> This is a kludge until I can fix issues #... I apologize for the complexity of this workaround but we need to put the value
     * into a Map<runId, runIdRescaleId> so that we can then re-parse it back out. It's not ideal but it's a lot easier to
     * manage as well as much faster and it gives us the ability to adjust this later. Beforehand this was
     * managed in the service layer and was a timebomb waiting to hit us. With this change I'll be able to refactor it in another step very
     * quickly. The only reason it's not included in this PR is that I'm trying to push any database changes to separate PRs.
     */
    public Map<Long, String> getRescaleRunJobIds(List<Long> runIds) {
        // Map the runIds/JobIds
        Map<Long, String> runIdsAndRescaleRunJobIdsMap = runIds.stream().collect(Collectors.toMap(
                    runId -> runId,
                    runId -> runIdKey(runId)));
        // Map the JobIds/value
        Map<String, String> rescaleRunJobIds = get(runIdsAndRescaleRunJobIdsMap.values().stream().collect(Collectors.toList()));
        // Map the runIds/value through JobIds
        return runIdsAndRescaleRunJobIdsMap.entrySet().stream().collect(Collectors.toMap(
                entry -> entry.getKey(),
                entry -> rescaleRunJobIds.get(entry.getValue())
        ));
    }

    // STEPH -> REFACTOR -> Was never called before. It was a service of a service but it was never ultimately called in the code.
    public void deleteRescaleRunJobId(Long runId) {
        delete(runIdKey(runId));
    }

    public void putModelFileKey(long modelId, Object value) {
        put(modelFileKey(modelId), value);
    }

    public String getModelFileKey(long modelId) {
        return get(modelFileKey(modelId));
    }

    // STEPH -> REFACTOR -> Was never called before. It was a service of a service but it was never ultimately called in the code.
    public void deleteModelFileKey(Long modelId) {
        delete(modelFileKey(modelId));
    }

    public void putCheckPointFileKey(String policyExternalId, Object value) {
        put(checkPointFileKey(policyExternalId), value);
    }

    public String getCheckPointFileKey(String policyExternalId) {
        return get(checkPointFileKey(policyExternalId));
    }

    private void put(String key, Object value) {
        ExecutionProviderMetaDataRepository.put(ctx, mapper, RESCALE_EXECUTION_PROVIDER, key, value);
    }

    private String get(String key) {
        return ExecutionProviderMetaDataRepository.get(ctx, RESCALE_EXECUTION_PROVIDER, key);
    }

    private Map<String, String> get(List<String> keys) {
        return ExecutionProviderMetaDataRepository.get(ctx, RESCALE_EXECUTION_PROVIDER, keys);
    }

    private void delete(String key) {
        ExecutionProviderMetaDataRepository.delete(ctx, RESCALE_EXECUTION_PROVIDER, key);
    }

    // STEPH -> REFACTOR -> Convert this to a column identifier so we can just do a where type=1, etc. for modelfile. So much easier and standard otherwise this will not scale up.
    private String modelFileKey(long modelId){
        return "modelFileId:" + modelId;
    }

    // STEPH -> REFACTOR -> Should be removed.
    private String runIdKey(long runId){
        return "runId:" + runId;
    }

    private String checkPointFileKey(String policyExternalId) {
        return "checkPointFileId:" + policyExternalId;
    }
}
