package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.db.jooq.tables.records.ModelRecord;
import lombok.Getter;
import org.jooq.TableField;

import java.util.HashMap;
import java.util.Map;

import static io.skymind.pathmind.db.jooq.tables.Model.MODEL;

public class ModelUpdateRequest {

    final Long modelId;

    final Map<TableField<ModelRecord, ?>, Object> updates = new HashMap<>();

    public ModelUpdateRequest(long modelId) {
        this.modelId = modelId;
    }

    public ModelUpdateRequest isDraft(Boolean isDraft) {
        updates.put(MODEL.DRAFT, isDraft);
        return this;
    }


    public ModelUpdateRequest userNotes(String userNotes) {
        updates.put(MODEL.USER_NOTES, userNotes);
        return this;
    }

    public ModelUpdateRequest hasGoals(boolean hasGoals) {
        updates.put(MODEL.HAS_GOALS, hasGoals);
        return this;
    }

    public ModelUpdateRequest name(String name) {
        updates.put(MODEL.NAME, name);
        return this;
    }

    public ModelUpdateRequest projectId(long projectId) {
        updates.put(MODEL.PROJECT_ID, projectId);
        return this;
    }

}
