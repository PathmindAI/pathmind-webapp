package io.skymind.pathmind.shared.constants;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public enum InvalidModelType {
    
    OLD_REWARD_VARIABLES(1);
    
    private int id;

    private InvalidModelType(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }

    private static final Map<Integer, InvalidModelType> BY_ID;
    static {
        Map<Integer,InvalidModelType> map = new ConcurrentHashMap<>();
        for (InvalidModelType instance : InvalidModelType.values()) {
            map.put(instance.getValue(), instance);
        }
        BY_ID = Collections.unmodifiableMap(map);
    }
    public static Optional<InvalidModelType> getEnumFromValue(int id) {
        return Optional.ofNullable(BY_ID.get(id));
    }
}
