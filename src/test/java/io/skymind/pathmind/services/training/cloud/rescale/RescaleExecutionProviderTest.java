package io.skymind.pathmind.services.training.cloud.rescale;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.services.training.ExecutionEnvironment;
import io.skymind.pathmind.services.training.JobSpec;
import io.skymind.pathmind.services.training.RunType;
import io.skymind.pathmind.services.training.cloud.rescale.api.RescaleRestApiClient;
import io.skymind.pathmind.services.training.db.metadata.ExecutionProviderMetaDataService;
import io.skymind.pathmind.services.training.versions.AnyLogic;
import io.skymind.pathmind.services.training.versions.PathmindHelper;
import io.skymind.pathmind.services.training.versions.RLLib;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Ignore
public class RescaleExecutionProviderTest {

    @Test
    public void testCreateWithUploadedModel(){
        final RescaleRestApiClient client = new RescaleRestApiClient("platform.rescale.jp", "0d0601925a547db44d41007e3cc4386b075c761c", new ObjectMapper(), WebClient.builder());
        final ExecutionProviderMetaDataService metaDataService = new ExecutionProviderMetaDataService() {

            @Override
            public void put(Class<?> providerClazz, String key, Object value) {
                throw new NotImplementedException("Not needed here");
            }

            @Override
            public <T> T get(Class<?> providerClazz, String key, Class<T> type) {
                final Map<String, String> map = Map.of(
                        "modelFileId:0", "GFHrBe"
                );
                return (T)map.get(key);
            }

            @Override
            public void delete(Class<?> providerClazz, String key) {
                throw new NotImplementedException("Not needed here");
            }
        };
        final RescaleExecutionProvider provider = new RescaleExecutionProvider(client, new RescaleMetaDataService(metaDataService));

        final ExecutionEnvironment env = new ExecutionEnvironment(
                AnyLogic.VERSION_8_5,
                PathmindHelper.VERSION_0_0_24,
                RLLib.VERSION_0_7_0
        );

        final JobSpec spec = new JobSpec(
                0, 0, 0, 0, "", "", "reward = -(before[0] - after[0]);", 4, 8, 100, env, RunType.TEST, null
        );

        System.out.println("provider.execute(spec, env) = " + provider.execute(spec));

    }

}