package io.skymind.pathmind.shared.constants;

import java.util.Arrays;
import java.util.Optional;

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
		Map<Integer,ObservationDataType> map = new ConcurrentHashMap<>();
		for (RunStatus instance : ObservationDataType.values()) {
			map.put(instance.getValue(), instance);
		}
		BY_NAME = Collections.unmodifiableMap(map);
    }
    public static Optional<ObservationDataType> getEnumFromValue(String value) {
        return Optional.ofNullable(BY_NAME.get(value));
    }
    
    public static boolean isNumeric(ObservationDataType dataType) {
        return dataType == NUMBER || dataType == INTEGER;
    }
    public static boolean isArray(ObservationDataType dataType) {
        return dataType == NUMBER_ARRAY || dataType == INTEGER_ARRAY;
    }
}
