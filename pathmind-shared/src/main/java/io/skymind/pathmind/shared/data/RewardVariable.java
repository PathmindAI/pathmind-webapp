package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.constants.GoalConditionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class RewardVariable extends Data {

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
}
