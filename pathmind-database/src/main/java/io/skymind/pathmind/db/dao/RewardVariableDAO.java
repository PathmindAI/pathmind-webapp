package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.db.utils.DBUtils;
import io.skymind.pathmind.shared.data.RewardVariable;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RewardVariableDAO {
    private final DSLContext ctx;

    RewardVariableDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void updateModelRewardVariables(long modelId, List<RewardVariable> rewardVariables) {
        ctx.transaction(configuration ->
        {
            DSLContext transactionCtx = DSL.using(configuration);
            DBUtils.setLockTimeout(transactionCtx, 4);
            RewardVariableRepository.deleteModelRewardsVariables(ctx, modelId);
            if (rewardVariables != null) {
                rewardVariables.forEach(rv -> rv.setModelId(modelId));
                RewardVariableRepository.insertOrUpdateRewardVariables(transactionCtx, rewardVariables);
            }
        });

    }

    public List<RewardVariable> getRewardVariablesForModel(long modelId) {
        return RewardVariableRepository.getRewardVariablesForModel(ctx, modelId);
    }
}
