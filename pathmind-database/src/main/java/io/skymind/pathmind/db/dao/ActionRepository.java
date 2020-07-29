package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.data.Action;
import org.jooq.DSLContext;
import org.jooq.Query;

import java.util.List;
import java.util.stream.Collectors;

import static io.skymind.pathmind.db.jooq.tables.Action.ACTION;

class ActionRepository {
    protected static void insertOrUpdateActions(DSLContext ctx, List<Action> actions) {
        final List<Query> saveQueries = actions.stream()
                .map(action ->
                        ctx.insertInto(ACTION)
                                .columns(ACTION.MODEL_ID, ACTION.NAME, ACTION.ARRAY_INDEX)
                                .values(action.getModelId(), action.getName(), action.getArrayIndex())
                                .onConflict(ACTION.MODEL_ID, ACTION.ARRAY_INDEX)
                                .doUpdate()
                                .set(ACTION.NAME, action.getName()))
                .collect(Collectors.toList());

        ctx.batch(saveQueries).execute();
    }

    protected static List<Action> getActionsForModel(DSLContext ctx, long modelId) {
        return ctx.select(ACTION.asterisk())
                .from(ACTION)
                .where(ACTION.MODEL_ID.eq(modelId))
                .fetchInto(Action.class);
    }
}
