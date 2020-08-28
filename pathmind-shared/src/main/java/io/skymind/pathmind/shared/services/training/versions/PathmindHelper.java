package io.skymind.pathmind.shared.services.training.versions;

import java.util.Arrays;
import java.util.List;

public enum PathmindHelper implements VersionEnum {
    VERSION_0_0_24,
    VERSION_0_0_25,
    VERSION_0_0_25_Multi,
    VERSION_1_0_1,
    VERSION_1_0_2,
    VERSION_1_2_0;

    @Override
    public List<String> fileNames() {
        return Arrays.asList("PathmindPolicy.jar");
    }
}
