package io.skymind.pathmind.api.domain.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectVO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("is_archived")
    private Boolean isArchived;

    @JsonProperty("date_created")
    private LocalDateTime dateCreated;

    @JsonProperty("date_last_activity")
    private LocalDateTime lastActivityDate;

//    private String userId;

}
