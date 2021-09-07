package io.skymind.pathmind.shared.constants;

import java.util.Arrays;

public enum  BatchMode {
    TRUNCATE_EPISODES("truncate_episodes"),
    COMPLETE_EPISODES("complete_episodes");

    private String name;

    BatchMode(String name) {
        this.name = name;
    }

    public static BatchMode fromName(String name) {
        return Arrays.stream(values())
            .filter(it -> it.name.equalsIgnoreCase(name))
            .findAny()
            .get();
    }

    @Override
    public String toString() {
        return name;
    }
}
