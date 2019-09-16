package io.skymind.pathmind.services.training.metadata;

public interface ExecutionProviderMetaDataService {
    public void put(Class<?> providerClazz, String key, Object value);
    public <T> T get(Class<?> providerClazz, String key, Class<T> type);
}
