package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.data.user.DeepCloneableInterface;
import io.skymind.pathmind.shared.utils.CloneUtils;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project extends ArchivableData implements DeepCloneableInterface {

    private static final long serialVersionUID = -8482276917940795086L;
    private LocalDateTime dateCreated;
    private LocalDateTime lastActivityDate;
    private String userNotes;

    private long pathmindUserId;

    // Helper GUI attributes not stored in the database
    private transient List<Model> models;

    @Override
    public Project shallowClone() {
        return super.shallowClone(Project.builder()
                .dateCreated(dateCreated)
                .lastActivityDate(lastActivityDate)
                .userNotes(userNotes)
                .pathmindUserId(pathmindUserId)
                .build());
    }

    @Override
    public Project deepClone() {
        Project project = shallowClone();
        project.setModels(CloneUtils.shallowCloneList(models));
        return project;
    }
}