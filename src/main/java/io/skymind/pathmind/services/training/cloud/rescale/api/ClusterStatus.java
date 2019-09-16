package io.skymind.pathmind.services.training.cloud.rescale.api;

import javax.validation.constraints.NotNull;

class ClusterStatus {
    @NotNull
    private String content;
    @NotNull
    private String labelClass;
    private boolean useLabel;

    // for deserialization
    public ClusterStatus(){}

    public ClusterStatus(@NotNull String content, @NotNull String labelClass, boolean useLabel) {
        this.content = content;
        this.labelClass = labelClass;
        this.useLabel = useLabel;
    }

    @NotNull
    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @NotNull
    public String getLabelClass() {
        return this.labelClass;
    }

    public void setLabelClass(String labelClass) {
        this.labelClass = labelClass;
    }

    public boolean getUseLabel() {
        return this.useLabel;
    }

    public void setUseLabel(boolean useLabel) {
        this.useLabel = useLabel;
    }
}
