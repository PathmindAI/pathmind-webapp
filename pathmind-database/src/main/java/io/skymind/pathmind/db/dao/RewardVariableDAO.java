package io.skymind.pathmind.db.dao;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import io.skymind.pathmind.shared.data.RewardVariable;

@Repository
public class RewardVariableDAO {
    private final DSLContext ctx;

    RewardVariableDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void updateModelRewardVariables(long modelId, List<RewardVariable> rewardVariables) {
        rewardVariables.forEach(rv -> rv.setModelId(modelId));
        ctx.transaction(conf -> {
            DSLContext transactionCtx = DSL.using(conf);
            RewardVariableRepository.insertOrUpdateRewardVariables(transactionCtx, rewardVariables);
        });
    }

    public List<RewardVariable> getRewardVariablesForModel(long modelId) {
        return RewardVariableRepository.getRewardVariablesForModel(ctx, modelId);
    }
}
