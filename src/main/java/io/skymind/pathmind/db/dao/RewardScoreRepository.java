package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.policy.RewardScore;
import io.skymind.pathmind.db.utils.JooqUtils;
import org.jooq.DSLContext;
import org.jooq.Query;
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
				.fetch(record -> new RewardScore(
						JooqUtils.getSafeDouble(record.get(REWARD_SCORE.MIN)),
						JooqUtils.getSafeDouble(record.get(REWARD_SCORE.MEAN)),
						JooqUtils.getSafeDouble(record.get(REWARD_SCORE.MAX)),
						record.get(REWARD_SCORE.ITERATION)));
    }

	protected static Map<Long, List<RewardScore>> getRewardScoresForPolicies(DSLContext ctx, List<Long> policyIds) {
		return ctx.select(REWARD_SCORE.MIN, REWARD_SCORE.MEAN, REWARD_SCORE.MAX, REWARD_SCORE.ITERATION, REWARD_SCORE.POLICY_ID)
				.from(REWARD_SCORE)
				.where(REWARD_SCORE.POLICY_ID.in(policyIds))
				.orderBy(REWARD_SCORE.POLICY_ID, REWARD_SCORE.ITERATION)
				.fetchGroups(REWARD_SCORE.POLICY_ID, record -> new RewardScore(
						JooqUtils.getSafeDouble(record.get(REWARD_SCORE.MIN)),
						JooqUtils.getSafeDouble(record.get(REWARD_SCORE.MEAN)),
						JooqUtils.getSafeDouble(record.get(REWARD_SCORE.MAX)),
						record.get(REWARD_SCORE.ITERATION)));
	}

	protected static int getMaxRewardScoreIteration(DSLContext ctx, long policyId) {
		return ctx.select(DSL.max(REWARD_SCORE.ITERATION))
				.from(REWARD_SCORE)
				.where(REWARD_SCORE.POLICY_ID.eq(policyId))
				.fetchOptional(0, Integer.class).orElse(0);
	}

	/**
	 * This is due to a limitation in JOOQ which does not allow dynamic multi-row inserts https://github.com/jOOQ/jOOQ/issues/6604
	 * we have to rely on batching.
	 */
	protected static void insertRewardScores(DSLContext ctx, long policyId, List<RewardScore> rewardScores) {
		List<Query> insertQueries = rewardScores.stream().map(rewardScore ->
				ctx.insertInto(REWARD_SCORE)
					.columns(REWARD_SCORE.MIN, REWARD_SCORE.MEAN, REWARD_SCORE.MAX, REWARD_SCORE.ITERATION, REWARD_SCORE.POLICY_ID)
					.values(JooqUtils.getSafeBigDecimal(rewardScore.getMin()),
							JooqUtils.getSafeBigDecimal(rewardScore.getMean()),
							JooqUtils.getSafeBigDecimal(rewardScore.getMax()),
							rewardScore.getIteration(),
							policyId)).collect(Collectors.toList());

		ctx.batch(insertQueries).execute();
	}

// 	FUTURE -> This is the expected path JOOQ will take when they offer dynamic multi-row inserts.
//	protected static void insertRewardScores(DSLContext ctx, Policy policy, int startIteration) {
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
//	}
}
