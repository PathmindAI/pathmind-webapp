package io.skymind.pathmind.webapp.data.utils;

import java.util.List;
import java.util.Optional;

import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.RewardVariable;

public class RewardVariablesUtils {
    public static void copyGoalsFromPreviousModel(RewardVariableDAO rewardVariableDAO, ModelDAO modelDAO, long projectId,
                                            long currentModelId, List<RewardVariable> currentRewardVariables) {
        Optional<Model> prevModel = modelDAO.getPrevModelForProject(projectId, currentModelId);
        prevModel.ifPresent(pm -> {
            List<RewardVariable> previousRewardVariables = rewardVariableDAO.getRewardVariablesForModel(pm.getId());
            currentRewardVariables.forEach(rv -> {
                Optional<RewardVariable> rvFromPrevModel = previousRewardVariables.stream().filter(prv -> prv.getName().equals(rv.getName())).findAny();
                rvFromPrevModel.ifPresent(prv -> {
                    rv.setGoalConditionType(prv.getGoalConditionType());
                    rv.setGoalValue(prv.getGoalValue());
                });
            });
        });
    }
}
