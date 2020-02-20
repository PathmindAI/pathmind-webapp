package io.skymind.pathmind.services.training.versions;

import java.util.Arrays;
import java.util.List;

public enum Conda implements VersionEnum {
    VERSION_0_7_0,
    VERSION_0_7_6,
    VERSION_0_8_1;

    @Override
    public List<String> fileNames() {
        return Arrays.asList("rllibpack.tar.gz");
    }
}
