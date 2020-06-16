package io.skymind.pathmind.db.dao;

import java.util.List;

import org.jooq.DSLContext;
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
        RewardVariableRepository.insertOrUpdateRewardVariables(ctx, rewardVariables);
    }

    public List<RewardVariable> getRewardVariablesForModel(long modelId) {
        return RewardVariableRepository.getRewardVariablesForModel(ctx, modelId);
    }
}
