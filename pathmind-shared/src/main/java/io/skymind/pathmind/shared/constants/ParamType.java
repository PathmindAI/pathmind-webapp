package io.skymind.pathmind.shared.constants;

import java.util.Arrays;

public enum ParamType {
    BOOLEAN(0, "boolean"),
    INTEGER(1, "integer"),
    DOUBLE(2, "double"),
    STRING(3, "String"),
    OTHERS(4, "others");

    private int id;
    private String name;

    ParamType(int id, String name) {
        this.id = id;
    }

    public String toString() {
        return this.name;
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
        } else {
            return OTHERS;
        }
    }

}
