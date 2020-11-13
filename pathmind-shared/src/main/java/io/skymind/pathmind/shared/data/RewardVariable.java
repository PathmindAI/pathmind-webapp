package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.data.user.DeepCloneableInterface;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RewardVariable extends Data implements DeepCloneableInterface<RewardVariable> {

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

    public RewardVariable shallowClone() {
        return super.shallowClone(new RewardVariable(
                modelId,
                arrayIndex,
                dataType,
                goalConditionType,
                goalValue));
    }
}
