package io.skymind.pathmind.db.dao;

import java.util.List;

import io.skymind.pathmind.shared.data.Model;
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

    public void updateModelAndRewardVariables(Model model, List<RewardVariable> rewardVariables) {
        rewardVariables.forEach(rv -> rv.setModelId(model.getId()));
        model.setHasGoals(rewardVariables.stream().anyMatch(rv -> rv.getGoalConditionType() != null));
        ctx.transaction(conf -> {
            DSLContext transactionCtx = DSL.using(conf);
            ModelRepository.updateHasGoals(transactionCtx, model.getId(), model.isHasGoals());
            RewardVariableRepository.insertOrUpdateRewardVariables(transactionCtx, rewardVariables);
        });
    }

    public List<RewardVariable> getRewardVariablesForModel(long modelId) {
        return RewardVariableRepository.getRewardVariablesForModel(ctx, modelId);
    }
}
