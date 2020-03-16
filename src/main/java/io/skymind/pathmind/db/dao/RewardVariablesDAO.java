package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.RewardVariable;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RewardVariablesDAO {
    private final DSLContext ctx;

    RewardVariablesDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void saveRewardVariables(List<RewardVariable> rewardVariables) {
        RewardVariablesRepository.insertRewardVariables(ctx, rewardVariables);
    }

    public List<RewardVariable> getRewardVariablesForModel(long modelId) {
        return RewardVariablesRepository.getRewardVariablesForModel(ctx, modelId);
    }
}
