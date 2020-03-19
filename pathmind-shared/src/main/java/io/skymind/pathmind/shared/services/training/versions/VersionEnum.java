package io.skymind.pathmind.shared.services.training.versions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface VersionEnum {
    List<String> fileNames();

    default List<String> convertPath() {
        return Hidden.paths(this);
    }

    class Hidden {
        private static List<String> paths(VersionEnum versionEnum) {
            List<String> paths = new ArrayList<>();
            versionEnum.fileNames().stream()
                    .forEach(file -> {
                        String folder = versionEnum.getClass().getSimpleName().toLowerCase();
                        String version = versionEnum.toString().split("VERSION_")[1].toLowerCase();
                        paths.add(String.join(File.separator, folder, version, file));

                    });

            return paths;
        }
    }
}
