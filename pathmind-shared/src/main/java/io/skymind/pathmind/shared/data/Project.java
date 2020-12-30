package io.skymind.pathmind.shared.data;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project extends ArchivableData {

    private static final long serialVersionUID = -8482276917940795086L;
    private LocalDateTime dateCreated;
    private LocalDateTime lastActivityDate;
    private String userNotes;
    private Integer modelCount;

    private long pathmindUserId;

    // Helper GUI attributes not stored in the database
    private transient List<Model> models;
}