package io.skymind.pathmind.shared.constants;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

@Getter
public enum GoalConditionType {
    GREATER_THAN_OR_EQUAL(
            "GTE", "≥",
            new RewardFunctionComponent("+", "maximize")
    ),
    LESS_THAN_OR_EQUAL("LTE", "≤",
            new RewardFunctionComponent("-", "minimize")
    ),
    ;

    private static final Map<String, GoalConditionType> BY_CODE;

    static {
        Map<String, GoalConditionType> map = new ConcurrentHashMap<>();
        for (GoalConditionType instance : GoalConditionType.values()) {
            map.put(instance.getCode(), instance);
        }
        BY_CODE = Collections.unmodifiableMap(map);
    }

    private final String code;
    private final String name;
    private final RewardFunctionComponent rewardFunctionComponent;

    GoalConditionType(String code, String name, RewardFunctionComponent rewardFunctionComponent) {
        this.code = code;
        this.name = name;
        this.rewardFunctionComponent = rewardFunctionComponent;
    }

    public static Optional<GoalConditionType> getEnumFromCode(String code) {
        return code == null ? Optional.empty() : Optional.ofNullable(BY_CODE.get(code));
    }

    public String toString() {
        return name;
    }
}
