package io.skymind.pathmind.services.training.cloud.rescale;

import io.skymind.pathmind.services.training.metadata.ExecutionProviderMetaDataService;
import org.springframework.stereotype.Service;

@Service
public class RescaleMetaDataService {

    private final ExecutionProviderMetaDataService metaDataService;

    public RescaleMetaDataService(ExecutionProviderMetaDataService service){
        this.metaDataService = service;
    }

    public void put(String key, Object value) {
        metaDataService.put(RescaleExecutionProvider.class, key, value);
    }

    public <T> T get(String key, Class<T> clazz) {
        return metaDataService.get(RescaleExecutionProvider.class, key, clazz);
    }

    public void delete(String key){
        metaDataService.delete(RescaleExecutionProvider.class, key);
    }

    public String modelFileKey(int modelId){
        return "modelFileId:" + modelId;
    }

    public String runIdKey(long runId){
        return "runId:" + runId;
    }
}
