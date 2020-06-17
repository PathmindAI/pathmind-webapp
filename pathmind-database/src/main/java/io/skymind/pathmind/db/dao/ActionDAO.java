package io.skymind.pathmind.db.dao;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import io.skymind.pathmind.shared.data.Action;

@Repository
public class ActionDAO {
    private final DSLContext ctx;

    ActionDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public List<Action> getActionsForModel(long modelId) {
        return ActionRepository.getActionsForModel(ctx, modelId);
    }
    
    public void updateModelActions(long modelId, List<Action> actions) {
        actions.forEach(a -> a.setModelId(modelId));
        ctx.transaction(conf -> {
            DSLContext transactionCtx = DSL.using(conf);
            ActionRepository.insertOrUpdateActions(transactionCtx, actions);
        });
    }
}
