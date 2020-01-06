package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.policy.RewardScore;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.skymind.pathmind.data.db.Tables.REWARD_SCORE;

class RewardScoreRepository
{
	protected static List<RewardScore> getRewardScoresForPolicy(DSLContext ctx, long policyId) {
        return ctx.select(REWARD_SCORE.MIN, REWARD_SCORE.MEAN, REWARD_SCORE.MAX, REWARD_SCORE.ITERATION)
				.from(REWARD_SCORE)
				.where(REWARD_SCORE.POLICY_ID.eq(policyId))
				.orderBy(REWARD_SCORE.ITERATION)
				.fetchInto(RewardScore.class);
    }

	protected static Map<Long, List<RewardScore>> getRewardScoresForPolicies(DSLContext ctx, List<Long> policyIds) {
		return ctx.select(REWARD_SCORE.MIN, REWARD_SCORE.MEAN, REWARD_SCORE.MAX, REWARD_SCORE.ITERATION, REWARD_SCORE.POLICY_ID)
				.from(REWARD_SCORE)
				.where(REWARD_SCORE.POLICY_ID.in(policyIds))
				.orderBy(REWARD_SCORE.POLICY_ID, REWARD_SCORE.ITERATION)
				.fetchGroups(REWARD_SCORE.POLICY_ID, RewardScore.class);
	}

	protected static int getMaxRewardScoreIteration(DSLContext ctx, long policyId) {
		return ctx.select(DSL.max(REWARD_SCORE.ITERATION))
				.from(REWARD_SCORE)
				.where(REWARD_SCORE.POLICY_ID.eq(policyId))
				.fetchOptional(0, Integer.class).orElseGet(() -> 0);
	}

	/**
	 * Only inserting new scores so we start from startIteration to the end of the list.
	 */
	protected static void updateRewardScores(DSLContext ctx, Policy policy, int startIteration) {
		ctx.insertInto(REWARD_SCORE)
				.columns(REWARD_SCORE.MIN, REWARD_SCORE.MEAN, REWARD_SCORE.MAX, REWARD_SCORE.ITERATION, REWARD_SCORE.POLICY_ID)
				.values(policy.getScores().subList(startIteration, policy.getScores().size()).stream()
						.map(rewardScore -> DSL.row(rewardScore.getMin(), rewardScore.getMean(), rewardScore.getMean(), rewardScore.getIteration(), policy.getId()))
						.collect(Collectors.toList()));
	}
}
