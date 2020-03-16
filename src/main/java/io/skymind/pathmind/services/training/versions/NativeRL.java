package io.skymind.pathmind.services.training.versions;

import java.util.Arrays;
import java.util.List;

public enum NativeRL implements VersionEnum{
    @Deprecated
    VERSION_0_7_0,
    VERSION_0_7_6,
    VERSION_0_7_6_PBT,
    VERSION_0_7_6_RESUME,
    VERSION_0_8_1;

    @Override
    public List<String> fileNames() {
        return Arrays.asList("nativerl-1.0.0-SNAPSHOT-bin.zip");
    }
}
