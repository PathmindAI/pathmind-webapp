package io.skymind.pathmind.db.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.skymind.pathmind.db.utils.JooqUtils;
import io.skymind.pathmind.shared.data.RewardScore;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Query;

import static io.skymind.pathmind.db.jooq.Tables.EXPERIMENT;
import static io.skymind.pathmind.db.jooq.Tables.POLICY;
import static io.skymind.pathmind.db.jooq.Tables.REWARD_SCORE;
import static io.skymind.pathmind.db.jooq.Tables.RUN;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.max;

@Slf4j
class RewardScoreRepository {
    protected static List<RewardScore> getRewardScoresForPolicy(DSLContext ctx, long policyId) {
        return ctx.select(REWARD_SCORE.MEAN, REWARD_SCORE.ITERATION, REWARD_SCORE.EPISODE_COUNT)
                .from(REWARD_SCORE)
                .where(REWARD_SCORE.POLICY_ID.eq(policyId))
                .orderBy(REWARD_SCORE.ITERATION)
                .fetch(record -> new RewardScore(
                        JooqUtils.getSafeDouble(record.get(REWARD_SCORE.MEAN)),
                        record.get(REWARD_SCORE.ITERATION),
                        record.get(REWARD_SCORE.EPISODE_COUNT)));
    }

    protected static Map<Long, List<RewardScore>> getRewardScoresForPolicies(DSLContext ctx, List<Long> policyIds) {
        log.trace("getRewardScoresForPolicies {}", policyIds);
        return ctx.select(REWARD_SCORE.MEAN, REWARD_SCORE.ITERATION, REWARD_SCORE.EPISODE_COUNT, REWARD_SCORE.POLICY_ID)
                .from(REWARD_SCORE)
                .where(REWARD_SCORE.POLICY_ID.in(policyIds))
                .orderBy(REWARD_SCORE.POLICY_ID, REWARD_SCORE.ITERATION)
                .fetchGroups(REWARD_SCORE.POLICY_ID, record -> new RewardScore(
                        JooqUtils.getSafeDouble(record.get(REWARD_SCORE.MEAN)),
                        record.get(REWARD_SCORE.ITERATION),
                        record.get(REWARD_SCORE.EPISODE_COUNT)));
    }

    protected static Map<Long, Integer> getRewardScoresCountForExperiments(DSLContext ctx, List<Long> experimentIds) {
        return ctx.select(EXPERIMENT.ID, count())
                .from(REWARD_SCORE)
                .leftJoin(POLICY).on(POLICY.ID.eq(REWARD_SCORE.POLICY_ID))
                .leftJoin(RUN).on(RUN.ID.eq(POLICY.RUN_ID))
                .leftJoin(EXPERIMENT).on(EXPERIMENT.ID.eq(RUN.EXPERIMENT_ID))
                .where(EXPERIMENT.ID.in(experimentIds))
                .groupBy(EXPERIMENT.ID)
                .fetchMap(EXPERIMENT.ID, count());
    }

    protected static Map<Long, Integer> getMaxRewardScoreIterationForPolicies(DSLContext ctx, List<Long> policyIds) {
        return ctx.select(REWARD_SCORE.POLICY_ID, max(REWARD_SCORE.ITERATION))
                .from(REWARD_SCORE)
                .where(REWARD_SCORE.POLICY_ID.in(policyIds))
                .groupBy(REWARD_SCORE.POLICY_ID)
                .fetchMap(REWARD_SCORE.POLICY_ID, max(REWARD_SCORE.ITERATION));
    }

    /**
     * This is due to a limitation in JOOQ which does not allow dynamic multi-row inserts https://github.com/jOOQ/jOOQ/issues/6604
     * we have to rely on batching.
     */
    protected static void insertRewardScores(DSLContext ctx, Map<Long, List<RewardScore>> rewardScoresByPolicyId) {
        List<Query> insertQueries = new ArrayList<>();
        rewardScoresByPolicyId.forEach((policyId, rewardScores) -> {
            List<Query> insertQueriesForPolicyId = rewardScores.stream().map(rewardScore ->
                    ctx.insertInto(REWARD_SCORE)
                            .columns(REWARD_SCORE.MEAN, REWARD_SCORE.ITERATION, REWARD_SCORE.POLICY_ID, REWARD_SCORE.EPISODE_COUNT)
                            .values(JooqUtils.getSafeBigDecimal(rewardScore.getMean()),
                                    rewardScore.getIteration(),
                                    policyId,
                                    rewardScore.getEpisodeCount())).collect(Collectors.toList());
            insertQueries.addAll(insertQueriesForPolicyId);
        });

        ctx.batch(insertQueries).execute();
    }

// 	FUTURE -> This is the expected path JOOQ will take when they offer dynamic multi-row inserts.
//	protected static void insertRewardScores(DSLContext ctx, Policy policy, int startIteration) {
//		ctx.insertInto(REWARD_SCORE)
//				.columns(REWARD_SCORE.MIN, REWARD_SCORE.MEAN, REWARD_SCORE.MAX, REWARD_SCORE.ITERATION, REWARD_SCORE.POLICY_ID, REWARD_SCORE.EPISODE_COUNT)
//				.values(policy.getScores().subList(startIteration, policy.getScores().size()).stream()
//						.map(rewardScore ->
//								DSL.row(JooqUtils.getSafeDouble(rewardScore.getMin()),
//										JooqUtils.getSafeDouble(rewardScore.getMean()),
//										JooqUtils.getSafeDouble(rewardScore.getMax()),
//										rewardScore.getIteration(),
//										policy.getId(),
//										rewardScore.getEpisodeCount()))
//						.collect(Collectors.toList()))
//				.execute();
//	}
}
