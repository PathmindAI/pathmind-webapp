package io.skymind.pathmind.shared.constants;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public enum ObservationDataType {
    NUMBER("number"), INTEGER("int"), BOOLEAN("boolean"), NUMBER_ARRAY("number[]"), INTEGER_ARRAY("int[]");
    
    private String name;

    private ObservationDataType(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public String getValue() {
        return name;
    }

    private static final Map<String, ObservationDataType> BY_NAME;
    static {
		Map<String,ObservationDataType> map = new ConcurrentHashMap<>();
		for (ObservationDataType instance : ObservationDataType.values()) {
			map.put(instance.getValue(), instance);
		}
		BY_NAME = Collections.unmodifiableMap(map);
    }
    public static Optional<ObservationDataType> getEnumFromValue(String value) {
        return value == null ? Optional.empty() : Optional.ofNullable(BY_NAME.get(value));
    }
    
    public static boolean isNumeric(ObservationDataType dataType) {
        return dataType == NUMBER || dataType == INTEGER;
    }
    public static boolean isArray(ObservationDataType dataType) {
        return dataType == NUMBER_ARRAY || dataType == INTEGER_ARRAY;
    }
}
