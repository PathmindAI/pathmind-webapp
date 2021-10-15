package io.skymind.pathmind.shared.data;

import java.util.Objects;

import io.skymind.pathmind.shared.constants.GoalConditionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RewardTerm extends Data implements DeepCloneableInterface<RewardTerm> {

    private Integer index;
    private Double weight;

    private Integer rewardVariableIndex;
    private GoalConditionType goalCondition;

    private String rewardSnippet;

    public RewardTerm(Integer index, Double weight, String rewardSnippet) {
        this(index, weight, null, null, rewardSnippet);
    }

    public RewardTerm(Integer index, Double weight, Integer rewardVariableIndex, GoalConditionType goalCondition) {
        this(index, weight, rewardVariableIndex, goalCondition, null);
    }

    public RewardTerm(Integer index, Double weight, Integer rewardVariableIndex, GoalConditionType goalCondition, String rewardSnippet) {
        this.index = index;
        this.weight = weight == null ? 1d : weight;
        this.rewardVariableIndex = rewardVariableIndex;
        this.goalCondition = goalCondition;
        this.rewardSnippet = rewardSnippet;
    }

    @Override
    public RewardTerm shallowClone() {
        return new RewardTerm(index, weight, rewardVariableIndex, goalCondition, rewardSnippet);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RewardTerm term = (RewardTerm) o;
        return Objects.equals(index, term.index)
                && Objects.equals(weight, term.weight)
                && Objects.equals(rewardVariableIndex, term.rewardVariableIndex)
                && goalCondition == term.goalCondition
                && Objects.equals(rewardSnippet, term.rewardSnippet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), index, weight, rewardVariableIndex, goalCondition, rewardSnippet);
    }

}
