package io.skymind.pathmind.db;

import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.RewardFunction;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.skymind.pathmind.data.db.tables.RewardFunction.REWARD_FUNCTION;

@Repository
public class RewardFunctionRepository
{
    @Autowired
    private DSLContext dslContext;

    public List<Project> getRewardFunctionForExperiment(long experimentId) {
    	return null;
//        return dslContext
//            .selectFrom(PROJECT)
//            .where(PROJECT.USER_ID.eq(userId))
//            .fetchInto(Project.class);
    }

    public RewardFunction getRewardFunction(long rewardFunctionId) {
        return dslContext
            .selectFrom(REWARD_FUNCTION)
            .where(REWARD_FUNCTION.ID.eq(rewardFunctionId))
            .fetchOneInto(RewardFunction.class);
    }

	public long insertRewardFunction(RewardFunction rewardfunction) {
    	return dslContext
				.insertInto(REWARD_FUNCTION)
				.set(REWARD_FUNCTION.NAME, rewardfunction.getName())
				.set(REWARD_FUNCTION.FUNCTION, rewardfunction.getFunction())
				.set(REWARD_FUNCTION.EXPERIMENT_ID, rewardfunction.getExperiment().getId())
				.returning(REWARD_FUNCTION.ID)
				.fetchOne()
				.getValue(REWARD_FUNCTION.ID);
	}
}
