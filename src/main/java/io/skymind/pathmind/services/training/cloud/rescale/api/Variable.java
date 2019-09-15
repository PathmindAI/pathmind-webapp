package io.skymind.pathmind.services.training.cloud.rescale.api;

import javax.validation.constraints.NotNull;

class Variable {
    @NotNull
    private final String displayName;
    private final boolean isRelative;
    @NotNull
    private final String name;
    private final float value;

    @NotNull
    public final String getDisplayName() {
        return this.displayName;
    }

    public final boolean isRelative() {
        return this.isRelative;
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    public final float getValue() {
        return this.value;
    }

    public Variable(@NotNull String displayName, boolean isRelative, @NotNull String name, float value) {
        this.displayName = displayName;
        this.isRelative = isRelative;
        this.name = name;
        this.value = value;
    }
}