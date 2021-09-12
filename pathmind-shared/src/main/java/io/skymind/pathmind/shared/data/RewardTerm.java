package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.constants.GoalConditionType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RewardTerm extends Data implements DeepCloneableInterface<RewardTerm> {

    private final Integer index;
    private final Double weight;

    private final Integer rewardVariableIndex;
    private final GoalConditionType goalConditionType;

    private final String rewardSnippet;

    public RewardTerm(Integer index, Double weight, String rewardSnippet) {
        this(index, weight, null, null, rewardSnippet);
    }

    public RewardTerm(Integer index, Double weight, Integer rewardVariableIndex, GoalConditionType goalConditionType) {
        this(index, weight, rewardVariableIndex, goalConditionType, null);
    }

    public RewardTerm(Integer index, Double weight, Integer rewardVariableIndex, GoalConditionType goalConditionType, String rewardSnippet) {
        this.index = index;
        this.weight = weight == null ? 1d : weight;
        this.rewardVariableIndex = rewardVariableIndex;
        this.goalConditionType = goalConditionType;
        this.rewardSnippet = rewardSnippet;
    }

    @Override
    public RewardTerm shallowClone() {
        return new RewardTerm(index, weight, rewardVariableIndex, goalConditionType, rewardSnippet);
    }

    @Override
    public RewardTerm deepClone() {
        return new RewardTerm(index, weight, rewardVariableIndex, goalConditionType, rewardSnippet);
    }

}
