package io.skymind.pathmind.shared.data;

import java.util.Objects;

import io.skymind.pathmind.shared.constants.GoalConditionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RewardVariable extends Data implements DeepCloneableInterface<RewardVariable> {

    public static final int DEFAULT_SELECTED_REWARD_VARIABLES = 1;
    public static final int MIN_SELECTED_REWARD_VARIABLES = 1;
    public static final int MAX_SELECTED_REWARD_VARIABLES = 2;

    private static final long serialVersionUID = 1963529929934242024L;
    private long modelId;
    private int arrayIndex;
    private String dataType;
    private String goalConditionType;
    private Double goalValue;

    public RewardVariable(long modelId, String name, int arrayIndex) {
        this(modelId, name, arrayIndex, "double");
    }

    public RewardVariable(long modelId, String name, int arrayIndex, String dataType) {
        this.modelId = modelId;
        this.arrayIndex = arrayIndex;
        this.dataType = dataType;
        setName(name);
    }

    // Used for the cloning method.
    private RewardVariable(long modelId, int arrayIndex, String dataType, String goalConditionType, Double goalValue) {
        this.modelId = modelId;
        this.arrayIndex = arrayIndex;
        this.dataType = dataType;
        this.goalConditionType = goalConditionType;
        this.goalValue = goalValue;
    }

    public GoalConditionType getGoalConditionTypeEnum() {
        return GoalConditionType.getEnumFromCode(goalConditionType).orElse(null);
    }

    public void setGoalConditionTypeEnum(GoalConditionType conditionType) {
        goalConditionType = conditionType != null ? conditionType.getCode() : null;
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
        RewardVariable that = (RewardVariable) o;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
    }

    public RewardVariable shallowClone() {
        return super.shallowClone(new RewardVariable(
                modelId,
                arrayIndex,
                dataType,
                goalConditionType,
                goalValue));
    }
}
