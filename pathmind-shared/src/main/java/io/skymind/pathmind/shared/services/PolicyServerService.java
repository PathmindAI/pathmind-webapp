package io.skymind.pathmind.shared.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.skymind.pathmind.shared.data.Experiment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface PolicyServerService {

    void saveSchemaYamlFile(String jobId, byte[] schemaYaml);

    byte[] getSchemaYamlFile(String jobId);

    void saveSchemaYamlFile(String jobId, PolicyServerSchema schema);

    void triggerPolicyServerDeployment(Experiment experiment);

    String getPolicyServerUrl(Experiment experiment);

    DeploymentStatus getPolicyServerStatus(Experiment experiment);

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
            @JsonProperty("url_path")
            private String urlPath;
        }
    }

    enum DeploymentStatus {
        NOT_DEPLOYED(0),
        PENDING(1),
        DEPLOYED(2),
        FAILED(3);

        public final int code;

        DeploymentStatus(int code) {
            this.code = code;
        }
    }

}
