package io.skymind.pathmind.services.training.versions;

import java.util.Arrays;
import java.util.List;

public enum AnyLogic implements VersionEnum {
    VERSION_8_5,
    VERSION_8_5_1,
    VERSION_8_5_2;

    @Override
    public List<String> fileNames() {
        return Arrays.asList("baseEnv.zip");
    }
}
