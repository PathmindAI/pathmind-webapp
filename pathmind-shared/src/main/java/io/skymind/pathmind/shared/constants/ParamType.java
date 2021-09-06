package io.skymind.pathmind.shared.constants;

import java.util.Arrays;
import java.util.Date;

public enum ParamType {

    BOOLEAN(0),
    INTEGER(1),
    DOUBLE(2),
    STRING(3),
    OTHERS(4),
    DATE(5);

    public static final String NULL_VALUE = "NULL_VALUE";

    private final int id;

    ParamType(int id) {
        this.id = id;
    }

    public int getValue() {
        return this.id;
    }

    public static ParamType getEnumFromValue(int value) {
        return Arrays.stream(values())
            .filter(paramType -> paramType.getValue() == value)
            .findAny()
            .get();
    }

    public static ParamType getEnumFromClass(Class klass) {
        if (klass == Boolean.class) {
            return BOOLEAN;
        } else if (klass == Integer.class) {
            return INTEGER;
        } else if (klass == Double.class) {
            return DOUBLE;
        } else if (klass == String.class) {
            return STRING;
        } else if (klass == Date.class) {
            return DATE;
        } else {
            return OTHERS;
        }
    }

}
