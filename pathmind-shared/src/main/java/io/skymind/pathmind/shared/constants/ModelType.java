package io.skymind.pathmind.shared.constants;

import java.util.Arrays;
import java.util.EnumSet;

public enum ModelType {
    AL_SINGLE(0, "al_single"),
    AL_MULTI(1, "al_multi"),
    PY_SINGLE(2, "py_single"),
    PY_MULTI(3, "py_multi");

    private int id;
    private String name;

    private static final EnumSet<ModelType> AL_TYPES = EnumSet.of(AL_SINGLE, AL_MULTI);
    private static final EnumSet<ModelType> PY_TYPES = EnumSet.of(PY_SINGLE, PY_MULTI);

    ModelType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public int getValue() {
        return id;
    }

    public static ModelType fromValue(int value) {
        return Arrays.stream(values())
                .filter(modelType -> modelType.getValue() == value)
                .findAny()
                .get();
    }

    public static ModelType fromName(String name) {
        // todo this is temporary code for support previous MA
        switch (name) {
            case "single":
                return AL_SINGLE;
            case "multi":
                return AL_MULTI;
        }

        return Arrays.stream(values())
                .filter(it -> it.name.equals(name))
                .findAny()
                .get();
    }

    public static boolean isALModel(ModelType type) {
        return AL_TYPES.contains(type);
    }

    public static boolean isPythonModel(ModelType type) {
        return PY_TYPES.contains(type);
    }
}
