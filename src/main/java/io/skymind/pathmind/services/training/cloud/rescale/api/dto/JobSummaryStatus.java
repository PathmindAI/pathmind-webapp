package io.skymind.pathmind.services.training.cloud.rescale.api.dto;

import javax.validation.constraints.NotNull;

class JobSummaryStatus {
    @NotNull
    private String content;
    @NotNull
    private String labelClass;
    @NotNull
    private String useLabel;

    // for deserialization
    private JobSummaryStatus(){}

    public JobSummaryStatus(@NotNull String content, @NotNull String labelClass, @NotNull String useLabel) {
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

    @NotNull
    public String getUseLabel() {
        return this.useLabel;
    }

    public void setUseLabel(String useLabel) {
        this.useLabel = useLabel;
    }
}
