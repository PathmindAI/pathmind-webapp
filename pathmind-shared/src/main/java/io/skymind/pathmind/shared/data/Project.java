package io.skymind.pathmind.shared.data;

import java.time.LocalDateTime;
import java.util.List;

import io.skymind.pathmind.shared.utils.CloneUtils;
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
public class Project extends ArchivableData implements DeepCloneableInterface<Project> {

    private static final long serialVersionUID = -8482276917940795086L;
    private LocalDateTime dateCreated;
    private LocalDateTime lastActivityDate;
    private String userNotes;
    private Integer modelCount;

    private long pathmindUserId;

    // Helper GUI attributes not stored in the database
    private transient List<Model> models;

    @Override
    public Project shallowClone() {
        Project project = super.shallowClone(Project.builder()
                .dateCreated(dateCreated)
                .lastActivityDate(lastActivityDate)
                .userNotes(userNotes)
                .pathmindUserId(pathmindUserId)
                .build());
        return project;
    }

    @Override
    public Project deepClone() {
        Project project = shallowClone();
        project.setModels(CloneUtils.shallowCloneList(models));
        return project;
    }
}