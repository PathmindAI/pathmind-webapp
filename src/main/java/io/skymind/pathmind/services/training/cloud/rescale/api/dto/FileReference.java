package io.skymind.pathmind.services.training.cloud.rescale.api.dto;

import javax.validation.constraints.NotNull;

public class FileReference {
    @NotNull
    private String id;
    private boolean decompress;

    // for deserialization
    private FileReference(){}

    public FileReference(@NotNull String id, boolean decompress) {
        this.id = id;
        this.decompress = decompress;
    }

    @NotNull
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getDecompress() {
        return this.decompress;
    }

    public void setDecompress(boolean decompress) {
        this.decompress = decompress;
    }
}