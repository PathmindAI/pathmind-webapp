package io.skymind.pathmind.services.training.versions;

import java.util.Arrays;
import java.util.List;

public enum JDK implements VersionEnum {
    VERSION_8_222;

    @Override
    public List<String> fileNames() {
        return Arrays.asList("OpenJDK8U-jdk_x64_linux_hotspot_8u222b10.tar.gz");
    }
}
