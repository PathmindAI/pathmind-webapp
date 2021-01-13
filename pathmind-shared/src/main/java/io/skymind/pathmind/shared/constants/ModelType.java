package io.skymind.pathmind.shared.constants;

import java.util.Arrays;

public enum ModelType {
    SINGLE(0, "single"),
    MULTI(1, "multi");

    private int id;
    private String name;

    ModelType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ModelType fromValue(int value) {
        return Arrays.stream(values())
                .filter(modelType -> modelType.getValue() == value)
                .findAny()
                .get();
    }

    public static ModelType fromName(String name) {
        return Arrays.stream(values())
                .filter(it -> it.name.equals(name))
                .findAny()
                .get();
    }

    public String toString() {
        return name;
    }

    public int getValue() {
        return id;
    }
}
