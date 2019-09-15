package io.skymind.pathmind.services.training.cloud.rescale.api;

import javax.validation.constraints.NotNull;

class ClusterStatus {
    @NotNull
    private final String content;
    @NotNull
    private final String labelClass;
    private final boolean useLabel;

    @NotNull
    public final String getContent() {
        return this.content;
    }

    @NotNull
    public final String getLabelClass() {
        return this.labelClass;
    }

    public final boolean getUseLabel() {
        return this.useLabel;
    }

    public ClusterStatus(@NotNull String content, @NotNull String labelClass, boolean useLabel) {
        this.content = content;
        this.labelClass = labelClass;
        this.useLabel = useLabel;
    }
}
