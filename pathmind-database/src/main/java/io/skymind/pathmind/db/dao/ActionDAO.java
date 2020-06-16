package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.data.Action;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        ctx.transaction(conf -> {
            DSLContext transactionCtx = DSL.using(conf);
            ActionRepository.deleteModelActions(transactionCtx, modelId);
            if (actions != null) {
                actions.forEach(a -> a.setModelId(modelId));
                ActionRepository.insertOrUpdateActions(transactionCtx, actions);
            }
        });
    }
}
