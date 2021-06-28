package io.skymind.pathmind.shared.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.shared.data.Run;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public interface PolicyServerService {

    void saveSchemaYamlFile(String jobId, byte[] schemaYaml);

    byte[] getSchemaYamlFile(String jobId);

    void saveSchemaYamlFile(String jobId, PolicyServerSchema schema);

    PolicyServerSchema generateSchemaYaml(Run run);

    void triggerPolicyServerDeployment(Experiment experiment);

    String getPolicyServerUrl(Experiment experiment);

    DeploymentStatus getPolicyServerStatus(Experiment experiment);

    @Getter
    class ObservationWrapper {
        public ObservationWrapper(Observation observation) {
            this.name = observation.getVariable();
            this.type = PolicyServerSchema.typeOf(observation);
            this.min_items = observation.getMaxItems();
            this.max_items = observation.getMaxItems();
        }

        @JsonIgnore
        private String name;
        private PolicyServerSchema.ObservationType type;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer min_items;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer max_items;
    }

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
        @JsonSerialize(using = ListObservationSerializer.class)
        private List<Observation> observations = new ArrayList<>();

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

            @JsonValue
            private final String type;

            public String getType() {
                return this.type;
            }
        }

        private static ObservationType typeOf(Observation observation) {
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
        public static final Map<Integer, DeploymentStatus> STATUS_BY_ID;

        DeploymentStatus(int code) {
            this.code = code;
        }

        static {
            Map<Integer, DeploymentStatus> map = new ConcurrentHashMap<>();
            for (DeploymentStatus instance : DeploymentStatus.values()) {
                map.put(instance.code, instance);
            }
            STATUS_BY_ID = Collections.unmodifiableMap(map);
        }

        public static final Set<DeploymentStatus> DEPLOYABLE = EnumSet.of(NOT_DEPLOYED, FAILED);
    }

    class ListObservationSerializer extends JsonSerializer<List<Observation>> {
        @Override
        public void serialize(List<Observation> observations, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            for (Observation obs : observations) {
                ObservationWrapper ow = new ObservationWrapper(obs);
                jsonGenerator.writeFieldName(ow.getName());
                jsonGenerator.writeObject(ow);
            }
            jsonGenerator.writeEndObject();
        }
    }

}
