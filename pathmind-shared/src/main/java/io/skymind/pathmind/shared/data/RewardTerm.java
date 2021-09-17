package io.skymind.pathmind.shared.data;

import java.util.Objects;

import io.skymind.pathmind.shared.constants.GoalConditionType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RewardTerm extends Data implements DeepCloneableInterface<RewardTerm> {

    private Integer index;
    private Double weight;

    private Integer rewardVariableIndex;
    private GoalConditionType goalConditionType;

    private String rewardSnippet;

    public RewardTerm(Integer index) {
        this(index, null, null, null, null);
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        RewardTerm term = (RewardTerm) o;
        return Objects.equals(index, term.index) // todo: do we need to include index into equals
                && Objects.equals(weight, term.weight)
                && Objects.equals(rewardVariableIndex, term.rewardVariableIndex)
                && goalConditionType == term.goalConditionType
                && Objects.equals(rewardSnippet, term.rewardSnippet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), index, weight, rewardVariableIndex, goalConditionType, rewardSnippet);
    }

}
