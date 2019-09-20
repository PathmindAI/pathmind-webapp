package io.skymind.pathmind.services.training.cloud.rescale.api.dto;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

class Analysis {
    @NotNull
    private String code;
    @Nullable
    private String version;

    // for deserialization
    private Analysis(){}

    public Analysis(@NotNull String code, @Nullable String version) {
        this.code = code;
        this.version = version;
    }

    public static Analysis userIncluded() {
        return new Analysis("user_included", "0");
    }

    @NotNull
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Nullable
    public String getVersion() {
        return this.version;
    }

    public void setVersion(@Nullable String version) {
        this.version = version;
    }
}