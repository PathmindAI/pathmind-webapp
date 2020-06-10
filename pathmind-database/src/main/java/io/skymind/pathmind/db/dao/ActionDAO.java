package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.data.Action;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActionDAO {
    private final DSLContext ctx;

    ActionDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void deleteModelActions(long id) {
        ActionRepository.deleteModelActions(ctx, id);
    }

    public List<Action> getActionsForModel(long modelId) {
        return ActionRepository.getActionsForModel(ctx, modelId);
    }

    public void saveActions(List<Action> actions) {
        ActionRepository.insertOrUpdateActions(ctx, actions);
    }
}
