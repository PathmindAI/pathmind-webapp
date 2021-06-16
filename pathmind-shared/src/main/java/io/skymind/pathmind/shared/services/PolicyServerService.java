package io.skymind.pathmind.shared.services;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Observation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

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

        @Builder.Default
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @JsonProperty("observations")
        private Map<String, ObservationType> observations = new HashMap<>();

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

        @JsonFormat(shape = JsonFormat.Shape.OBJECT)
        public enum ObservationType {
            INT("int"),
            INT_ARRAY("List[int]"),
            FLOAT("float"),
            FLOAT_ARRAY("List[float]"),
            BOOL("bool"),
            BOOL_ARRAY("List[bool]"),
            ;

            ObservationType(String type) {
                this.type = type;
            }

            private final String type;

            public String getType() {
                return this.type;
            }

        }

        public static ObservationType typeOf(Observation observation) {
            switch (observation.getDataTypeEnum()) {
                case INTEGER:
                case LONG:
                    return ObservationType.INT;
                case INTEGER_ARRAY:
                case LONG_ARRAY:
                    return ObservationType.INT_ARRAY;
                case NUMBER:
                case FLOAT:
                    return ObservationType.FLOAT;
                case NUMBER_ARRAY:
                case FLOAT_ARRAY:
                    return ObservationType.FLOAT_ARRAY;
                case BOOLEAN:
                    return ObservationType.BOOL;
                case BOOLEAN_ARRAY:
                    return ObservationType.BOOL_ARRAY;
                default:
                    throw new IllegalArgumentException("Unknown observation type " + observation.getDataType());
            }
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
