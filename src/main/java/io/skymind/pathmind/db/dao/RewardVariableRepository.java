package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.RewardVariable;
import org.jooq.DSLContext;
import org.jooq.Query;

import java.util.List;
import java.util.stream.Collectors;

import static io.skymind.pathmind.data.db.Tables.REWARD_VARIABLE;

class RewardVariableRepository {
    private RewardVariableRepository() {
    }

    static void insertRewardVariables(DSLContext ctx, List<RewardVariable> rewardVariables) {
        final List<Query> insertQueries = rewardVariables.stream()
                .map(rewardVariable ->
                        ctx.insertInto(REWARD_VARIABLE)
                                .columns(REWARD_VARIABLE.MODEL_ID, REWARD_VARIABLE.NAME, REWARD_VARIABLE.ARRAY_INDEX)
                                .values(rewardVariable.getModelId(), rewardVariable.getName(), rewardVariable.getArrayIndex()))
                .collect(Collectors.toList());

        ctx.batch(insertQueries).execute();
    }

    static List<RewardVariable> getRewardVariablesForModel(DSLContext ctx, long modelId) {
        return ctx.select(REWARD_VARIABLE.asterisk())
                .from(REWARD_VARIABLE)
                .where(REWARD_VARIABLE.MODEL_ID.eq(modelId))
                .fetchInto(RewardVariable.class);
    }
}
