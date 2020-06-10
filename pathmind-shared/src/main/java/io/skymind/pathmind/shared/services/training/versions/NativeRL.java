package io.skymind.pathmind.shared.services.training.versions;

import java.util.Arrays;
import java.util.List;

public enum NativeRL implements VersionEnum{
    @Deprecated
    VERSION_0_7_0,
    VERSION_0_7_6,
    VERSION_0_7_6_PBT,
    VERSION_0_7_6_RESUME,
    VERSION_0_8_1,
    VERSION_1_0_1,
    VERSION_1_0_3,
    VERSION_1_0_4,
    VERSION_1_0_5,
    VERSION_1_0_6,
    VERSION_1_0_7;

    @Override
    public List<String> fileNames() {
        return Arrays.asList("nativerl-1.0.0-SNAPSHOT-bin.zip");
    }
}
