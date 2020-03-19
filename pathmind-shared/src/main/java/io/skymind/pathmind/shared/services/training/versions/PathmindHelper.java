package io.skymind.pathmind.shared.services.training.versions;

import java.util.Arrays;
import java.util.List;

public enum PathmindHelper implements VersionEnum {
    VERSION_0_0_24,
    VERSION_0_0_25,
    VERSION_0_0_25_Multi;

    @Override
    public List<String> fileNames() {
        return Arrays.asList("PathmindPolicy.jar");
    }
}
