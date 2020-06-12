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

    public static Optional<ObservationDataType> getEnumFromValue(String value) {
        return Arrays.stream(values())
                .filter(dataType -> dataType.getValue().equals(value))
                .findAny();
    }
}
