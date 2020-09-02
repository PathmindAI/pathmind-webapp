package io.skymind.pathmind.shared.constants;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public enum GoalConditionType {
    GREATER_THAN_OR_EQUAL("GTE", ">="), LESS_THAN_OR_EQUAL("LTE", "<=");
    
    private String code;
    private String name;

    private GoalConditionType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public String getValue() {
        return code;
    }

    private static final Map<String, GoalConditionType> BY_CODE;
    static {
		Map<String,GoalConditionType> map = new ConcurrentHashMap<>();
		for (GoalConditionType instance : GoalConditionType.values()) {
			map.put(instance.getValue(), instance);
		}
		BY_CODE = Collections.unmodifiableMap(map);
    }
    public static Optional<GoalConditionType> getEnumFromCode(String code) {
        return code == null ? Optional.empty() : Optional.ofNullable(BY_CODE.get(code));
    }
}
