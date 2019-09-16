package io.skymind.pathmind.services.training.cloud.rescale.api;

import javax.validation.constraints.NotNull;

class Variable {
    @NotNull
    private String displayName;
    private boolean isRelative;
    @NotNull
    private String name;
    private float value;

    // for deserialization
    private Variable(){}

    public Variable(@NotNull String displayName, boolean isRelative, @NotNull String name, float value) {
        this.displayName = displayName;
        this.isRelative = isRelative;
        this.name = name;
        this.value = value;
    }

    @NotNull
    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isRelative() {
        return this.isRelative;
    }

    public void setRelative(boolean relative) {
        isRelative = relative;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return this.value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}