package io.skymind.pathmind.services.training.cloud.rescale.api;

import javax.validation.constraints.NotNull;

class FileReference {
    @NotNull
    private final String id;
    private final boolean decompress;

    @NotNull
    public final String getId() {
        return this.id;
    }

    public final boolean getDecompress() {
        return this.decompress;
    }

    public FileReference(@NotNull String id, boolean decompress) {
        this.id = id;
        this.decompress = decompress;
    }
}