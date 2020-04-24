package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.data.RewardVariable;
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
        RewardVariableRepository.insertOrUpdateRewardVariables(ctx, rewardVariables);
    }

    public List<RewardVariable> getRewardVariablesForModel(long modelId) {
        return RewardVariableRepository.getRewardVariablesForModel(ctx, modelId);
    }

    public void deleteModelRewardVariables(long modelId) {
        RewardVariableRepository.deleteModelRewardsVariables(ctx, modelId);
    }
}
