package io.skymind.pathmind.db.dao;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import io.skymind.pathmind.db.jooq.tables.records.ProjectRecord;
import org.jooq.TableField;

import static io.skymind.pathmind.db.jooq.tables.Project.PROJECT;

public class ProjectUpdateRequest {

    final Long projectId;

    final Map<TableField<ProjectRecord, ?>, Object> updates = new HashMap<>();

    public ProjectUpdateRequest(long projectId) {
        this.projectId = projectId;
    }

    public ProjectUpdateRequest isArchived(boolean isArchive) {
        updates.put(PROJECT.ARCHIVED, isArchive);
        return this;
    }

    public ProjectUpdateRequest name(String projectName) {
        updates.put(PROJECT.NAME, projectName);
        return this;
    }

    public ProjectUpdateRequest userNotes(String userNotes) {
        updates.put(PROJECT.USER_NOTES, userNotes);
        return this;
    }

    public ProjectUpdateRequest lastActivityDate(LocalDateTime activityDate) {
        updates.put(PROJECT.LAST_ACTIVITY_DATE, activityDate);
        return this;
    }

}
