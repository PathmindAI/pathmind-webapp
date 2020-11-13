package io.skymind.pathmind.api.domain.project.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
