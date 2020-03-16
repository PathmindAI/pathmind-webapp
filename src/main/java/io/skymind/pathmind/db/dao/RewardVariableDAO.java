package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.RewardVariable;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RewardVariableDAO {
    private final DSLContext ctx;

    RewardVariableDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void saveRewardVariables(List<RewardVariable> rewardVariables) {
        RewardVariableRepository.insertRewardVariables(ctx, rewardVariables);
    }

    public List<RewardVariable> getRewardVariablesForModel(long modelId) {
        return RewardVariableRepository.getRewardVariablesForModel(ctx, modelId);
    }
}
