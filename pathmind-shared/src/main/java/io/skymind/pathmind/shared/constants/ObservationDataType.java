package io.skymind.pathmind.shared.constants;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public enum ObservationDataType {
    NUMBER("number"), NUMBER_ARRAY("number[]"),
    INTEGER("int"), INTEGER_ARRAY("int[]"),
    FLOAT("float"), FLOAT_ARRAY("float[]"),
    LONG("long"), LONG_ARRAY("long[]"),
    BOOLEAN("boolean"), BOOLEAN_ARRAY("boolean[]");
    
    private String name;

    ObservationDataType(String name) {
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
        return !isArray(dataType);
    }
    public static boolean isArray(ObservationDataType dataType) {
        return dataType.name.endsWith("[]");
    }
}
