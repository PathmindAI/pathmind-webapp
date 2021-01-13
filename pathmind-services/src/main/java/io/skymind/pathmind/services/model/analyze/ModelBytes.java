package io.skymind.pathmind.services.model.analyze;


import java.io.IOException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ModelBytes {

    private final String error;
    private final byte[] bytes;

    private ModelBytes(byte[] bytes, String error) {
        this.bytes = bytes;
        this.error = error;
    }

    public static ModelBytes of(byte[] bytes) throws IOException {
        return new ModelBytes(bytes, null);
    }

    public static ModelBytes error(String error) {
        return new ModelBytes(null, error);
    }

}
