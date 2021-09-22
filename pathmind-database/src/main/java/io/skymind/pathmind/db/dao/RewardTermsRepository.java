package io.skymind.pathmind.db.dao;

import java.util.ArrayList;
import java.util.List;

import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.data.RewardTerm;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Query;

import static io.skymind.pathmind.db.jooq.Tables.REWARD_TERM;

@Slf4j
class RewardTermsRepository {

    public static void flushAndSaveTerms(DSLContext ctx, long experimentId, List<RewardTerm> rewardTerms) {

        final List<Query> queries = new ArrayList<>();

        Query deleteAll = ctx.deleteFrom(REWARD_TERM).where(REWARD_TERM.EXPERIMENT_ID.eq(experimentId));
        queries.add(deleteAll);

        rewardTerms.stream().map(term ->
            ctx.insertInto(REWARD_TERM)
                    .columns(
                            REWARD_TERM.EXPERIMENT_ID, REWARD_TERM.INDEX, REWARD_TERM.WEIGHT,
                            REWARD_TERM.REWARD_VARIABLE_ARRAY_INDEX, REWARD_TERM.GOAL_CONDITION_TYPE,
                            REWARD_TERM.SNIPPET
                    )
                    .values(
                            experimentId, term.getIndex(), term.getWeight(),
                            term.getRewardVariableIndex(), term.getGoalCondition() == null ? null : term.getGoalCondition().getCode(),
                            term.getRewardSnippet()
                    )
        ).forEach(queries::add);

        ctx.batch(queries).execute();
    }

    public static List<RewardTerm> getRewardTerms(DSLContext ctx, long experimentId) {

        return ctx.selectFrom(REWARD_TERM)
                .where(REWARD_TERM.EXPERIMENT_ID.eq(experimentId))
                .orderBy(REWARD_TERM.INDEX)
                .fetch(record -> new RewardTerm(
                        record.get(REWARD_TERM.INDEX),
                        record.get(REWARD_TERM.WEIGHT),
                        record.get(REWARD_TERM.REWARD_VARIABLE_ARRAY_INDEX),
                        GoalConditionType.getEnumFromCode(record.get(REWARD_TERM.GOAL_CONDITION_TYPE)).orElse(null),
                        record.get(REWARD_TERM.SNIPPET)
                ));
    }

}
