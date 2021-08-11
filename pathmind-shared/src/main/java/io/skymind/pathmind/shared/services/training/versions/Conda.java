package io.skymind.pathmind.shared.services.training.versions;

import java.util.Arrays;
import java.util.List;

/**
 * VERSION_0_7_0: RAY 0.7.0 and TF 1.13.1(deprecated),
 * VERSION_0_7_6: RAY 0.7.6 and TF 1.13.1,
 * VERSION_0_8_1: RAY 0.8.1 and TF 1.15.0
 */
public enum Conda implements VersionEnum {
    @Deprecated
    VERSION_0_7_0,
    VERSION_0_7_6,
    VERSION_0_8_1,
    VERSION_0_8_5,
    VERSION_0_8_6,
    VERSION_0_8_7,
    VERSION_1_0_0,
    VERSION_1_2_0,
    VERSION_1_3_0,
    VERSION_1_4_0,
    VERSION_1_5_0;

    @Override
    public List<String> fileNames() {
        return Arrays.asList("rllibpack.tar.gz");
    }
}
