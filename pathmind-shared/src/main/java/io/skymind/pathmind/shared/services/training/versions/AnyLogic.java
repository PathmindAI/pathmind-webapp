package io.skymind.pathmind.shared.services.training.versions;

import java.util.Arrays;
import java.util.List;

public enum AnyLogic implements VersionEnum {
    VERSION_8_5,
    VERSION_8_5_1,
    VERSION_8_5_2,
    VERSION_8_6_0,
    VERSION_8_6_1,
    VERSION_8_7_0,
    VERSION_8_7_3,
    VERSION_8_7_4;

    @Override
    public List<String> fileNames() {
        return Arrays.asList("baseEnv.zip");
    }
}
