package io.skymind.pathmind.shared.data.rllib;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.shared.utils.ObjectMapperHolder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
@NoArgsConstructor
public class ExperimentState {
    @JsonProperty("checkpoints")
    private List<String> chkpStrs;
    private List<CheckPoint> checkpoints = null;

    public ExperimentState(List<CheckPoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public List<CheckPoint> getCheckpoints() {
        if (checkpoints == null) {
            ObjectMapper objectMapper = ObjectMapperHolder.getJsonMapper();
            checkpoints = new ArrayList<>();

            chkpStrs.forEach(str -> {
                try {
                    checkpoints.add(objectMapper.readValue(str, CheckPoint.class));
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage(), e);
                }
            });
        }

        return checkpoints;
    }
}


