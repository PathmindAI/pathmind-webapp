package io.skymind.pathmind.services.training.cloud.aws.api.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEvent implements Serializable {

    public static final String CARGO_ATTRIBUTE = "cargo";
    public static final String FILTER_ATTRIBUTE = "filter";

    public static final String TYPE_POLICY = "policy";
    public static final String TYPE_RUN = "run";

    @JsonProperty("id")
    private Long id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("size")
    private Integer size;
}

