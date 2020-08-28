package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.data.RewardVariable;
import org.jooq.DSLContext;
import org.jooq.Query;

import java.util.List;
import java.util.stream.Collectors;

import static io.skymind.pathmind.db.jooq.tables.RewardVariable.REWARD_VARIABLE;


class RewardVariableRepository {
    private RewardVariableRepository() {
    }

    static void insertOrUpdateRewardVariables(DSLContext ctx, List<RewardVariable> rewardVariables) {
        final List<Query> saveQueries = rewardVariables.stream()
                .map(rewardVariable ->
                        ctx.insertInto(REWARD_VARIABLE)
                                .columns(REWARD_VARIABLE.MODEL_ID, REWARD_VARIABLE.NAME, REWARD_VARIABLE.ARRAY_INDEX, REWARD_VARIABLE.DATA_TYPE)
                                .values(rewardVariable.getModelId(), rewardVariable.getName(), rewardVariable.getArrayIndex(), rewardVariable.getDataType())
                                .onConflict(REWARD_VARIABLE.MODEL_ID, REWARD_VARIABLE.ARRAY_INDEX)
                                .doUpdate()
                                .set(REWARD_VARIABLE.NAME, rewardVariable.getName()))
                .collect(Collectors.toList());

        ctx.batch(saveQueries).execute();
    }

    static List<RewardVariable> getRewardVariablesForModel(DSLContext ctx, long modelId) {
        return ctx.select(REWARD_VARIABLE.asterisk())
                .from(REWARD_VARIABLE)
                .where(REWARD_VARIABLE.MODEL_ID.eq(modelId))
                .fetchInto(RewardVariable.class);
    }
}
