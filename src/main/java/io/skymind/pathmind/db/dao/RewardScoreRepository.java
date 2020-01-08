package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.db.tables.records.RewardScoreRecord;
import io.skymind.pathmind.data.policy.RewardScore;
import io.skymind.pathmind.db.utils.JooqUtils;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep5;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
				.fetchOptional(0, Integer.class).orElse(0);
	}

	/**
	 * Only inserting new scores so we start from startIteration to the end of the list.
	 */
	protected static void insertRewardScores(DSLContext ctx, Policy policy, int startIteration) {
		Optional<Integer> result = Optional.of(
			ctx.insertInto(REWARD_SCORE)
				.columns(REWARD_SCORE.MIN, REWARD_SCORE.MEAN, REWARD_SCORE.MAX, REWARD_SCORE.ITERATION, REWARD_SCORE.POLICY_ID))
				.map(statement -> {
							policy.getScores().subList(startIteration, policy.getScores().size()).stream().forEach(rewardScore -> {
								statement.values(new BigDecimal(JooqUtils.getSafeDouble(rewardScore.getMin())),
										new BigDecimal(JooqUtils.getSafeDouble(rewardScore.getMean())),
										new BigDecimal(JooqUtils.getSafeDouble(rewardScore.getMax())),
										rewardScore.getIteration(),
										policy.getId());
							});
							return statement.execute();
						});


//		ctx.insertInto(REWARD_SCORE)
//				.columns(REWARD_SCORE.MIN, REWARD_SCORE.MEAN, REWARD_SCORE.MAX, REWARD_SCORE.ITERATION, REWARD_SCORE.POLICY_ID)
//				.values(policy.getScores().subList(startIteration, policy.getScores().size()).stream()
//						.map(rewardScore ->
//								DSL.row(JooqUtils.getSafeDouble(rewardScore.getMin()),
//										JooqUtils.getSafeDouble(rewardScore.getMean()),
//										JooqUtils.getSafeDouble(rewardScore.getMax()),
//										rewardScore.getIteration(),
//										policy.getId()))
//						.collect(Collectors.toList()))
//				.execute();
	}
}
