package io.skymind.pathmind.shared.data.rllib;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Setter;

@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckPoint {
    public static final String TERMINATED = "TERMINATED";
    public static final String RUNNING = "RUNNING";
    public static final String ERROR = "ERROR";
    private String status;
    private double last_update_time;
    @JsonProperty("logdir")
    @JsonDeserialize(using = IDCustomDeserializer.class)
    private String id;

    public String getStatus() {
        return status;
    }

    public long getLastUpdateTime() {
        // ray returns time for seconds
        return ((long) (last_update_time * 1000));
    }

    public String getId() {
        return id;
    }
}

class IDCustomDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String logDir = jsonParser.getText();
        return new File(logDir).getName();
    }
}
