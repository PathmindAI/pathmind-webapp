package io.skymind.pathmind.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface PolicyServerService {
    String createSchemaYaml(long modelId);

    void saveSchemaYamlFile(long modelId, byte[] schemaYaml);

    byte[] getSchemaYamlFile(long modelId);

    void saveSchemaYamlFile(long modelId, PolicyServerSchema schema);

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class PolicyServerSchema {

        @JsonProperty("parameters")
        private Parameters parameters;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Parameters {
            @JsonProperty("discrete")
            private boolean discrete;
            @JsonProperty("tuple")
            private boolean tuple;
            @JsonProperty("api_key")
            private String apiKey;
        }
    }
}
