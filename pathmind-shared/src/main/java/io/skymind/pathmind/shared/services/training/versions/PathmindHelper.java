package io.skymind.pathmind.shared.services.training.versions;

import java.util.Arrays;
import java.util.List;

public enum PathmindHelper implements VersionEnum {
    VERSION_1_0_2,
    VERSION_1_2_0,
    VERSION_1_3_0,
    VERSION_1_4_0;

    @Override
    public List<String> fileNames() {
        return Arrays.asList("PathmindPolicy.jar");
    }
}
