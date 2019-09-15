package io.skymind.pathmind.services.training.cloud.rescale.api;

import javax.validation.constraints.NotNull;

class JobSummaryStatus {
    @NotNull
    private final String content;
    @NotNull
    private final String labelClass;
    @NotNull
    private final String useLabel;

    @NotNull
    public final String getContent() {
        return this.content;
    }

    @NotNull
    public final String getLabelClass() {
        return this.labelClass;
    }

    @NotNull
    public final String getUseLabel() {
        return this.useLabel;
    }

    public JobSummaryStatus(@NotNull String content, @NotNull String labelClass, @NotNull String useLabel) {
        this.content = content;
        this.labelClass = labelClass;
        this.useLabel = useLabel;
    }
}
