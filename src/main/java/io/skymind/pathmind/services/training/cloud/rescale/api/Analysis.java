package io.skymind.pathmind.services.training.cloud.rescale.api;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

class Analysis {
    @NotNull
    private final String code;
    @Nullable
    private final String version;

    @NotNull
    public final String getCode() {
        return this.code;
    }

    @Nullable
    public final String getVersion() {
        return this.version;
    }

    public Analysis(@NotNull String code, @Nullable String version) {
        this.code = code;
        this.version = version;
    }

    public static Analysis userIncluded(){
        return new Analysis("user_included", "0");
    }
}