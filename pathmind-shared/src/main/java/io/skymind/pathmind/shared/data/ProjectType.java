package io.skymind.pathmind.shared.data;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

public enum ProjectType {
    REGULAR(0),
    DEMO(1),
    ;

    private static final Map<Integer, ProjectType> TYPES;
    static {
        Map<Integer, ProjectType> map = new ConcurrentHashMap<>();
        for (ProjectType instance : ProjectType.values()) {
            map.put(instance.getCode(), instance);
        }
        TYPES = Collections.unmodifiableMap(map);
    }

    ProjectType(int code) {
        this.code = code;
    }

    @Getter
    private int code;

    public static ProjectType of(int code) {
        return TYPES.get(code);
    }
}
