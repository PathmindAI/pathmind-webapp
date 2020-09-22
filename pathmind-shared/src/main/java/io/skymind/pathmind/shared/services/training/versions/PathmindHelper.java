package io.skymind.pathmind.shared.services.training.versions;

import java.util.Arrays;
import java.util.List;

public enum PathmindHelper implements VersionEnum {
    @Deprecated
    VERSION_0_0_24,
    @Deprecated
    VERSION_0_0_25,
    @Deprecated
    VERSION_0_0_25_Multi,
    @Deprecated
    VERSION_1_0_1,
    @Deprecated
    VERSION_1_0_2,
    VERSION_1_2_0;

    @Override
    public List<String> fileNames() {
        return Arrays.asList("PathmindPolicy.jar");
    }
}
