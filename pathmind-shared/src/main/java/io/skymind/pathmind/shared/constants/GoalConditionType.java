package io.skymind.pathmind.shared.constants;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

@Getter
public enum GoalConditionType {
    GREATER_THAN_OR_EQUAL("GTE", "≥", "+"), LESS_THAN_OR_EQUAL("LTE", "≤", "-");

    private final String code;
    private final String name;
    private final String mathOperation;

    GoalConditionType(String code, String name, String math) {
        this.code = code;
        this.name = name;
        this.mathOperation = math;
    }

    public String toString() {
        return name;
    }

    private static final Map<String, GoalConditionType> BY_CODE;

    static {
        Map<String, GoalConditionType> map = new ConcurrentHashMap<>();
        for (GoalConditionType instance : GoalConditionType.values()) {
            map.put(instance.getCode(), instance);
        }
        BY_CODE = Collections.unmodifiableMap(map);
    }

    public static Optional<GoalConditionType> getEnumFromCode(String code) {
        return code == null ? Optional.empty() : Optional.ofNullable(BY_CODE.get(code));
    }
}
