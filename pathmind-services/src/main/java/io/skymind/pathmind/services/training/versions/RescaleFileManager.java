package io.skymind.pathmind.services.training.versions;

import io.skymind.pathmind.services.training.cloud.rescale.api.dto.FileReference;
import io.skymind.pathmind.shared.services.training.versions.AnyLogic;
import io.skymind.pathmind.shared.services.training.versions.NativeRL;
import io.skymind.pathmind.shared.services.training.versions.PathmindHelper;
import io.skymind.pathmind.shared.services.training.versions.VersionEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RescaleFileManager {
    private static RescaleFileManager instance;

    public static RescaleFileManager getInstance() {
        if (instance == null) {
            instance = new RescaleFileManager();
        }

        return instance;
    }

    private RescaleFileManager() {
        setData();
    }

    private static final Map<String, Map<VersionEnum, List<String>>> versions = new HashMap<>();
    private String currentMode = "PROD";

    private void setData() {
        Map<VersionEnum, List<String>> vTable = new HashMap<>();
        vTable.put(AnyLogic.VERSION_8_5, Arrays.asList(
                "nbwVpg" // Anylogic 8.5 Base Environment: baseEnv.zip
        ));
        vTable.put(AnyLogic.VERSION_8_5_1, Arrays.asList(
                "FcrKm" // Anylogic 8.5.1 Base Environment: baseEnv.zip
        ));
        vTable.put(NativeRL.VERSION_0_7_0, Arrays.asList(
//                "LZAENb", // conda
//                "jniWLb", // conda TF 2.0, RAY 0.7.6
                "xGfzT", // conda TF 1.13.1, RAY 0.7.6
//                "jKjXa", // nativerl-1.0.0-SNAPSHOT-bin.zip, 2019-10-15 DH version
//                "OoYSOb", // nativerl-1.0.0-SNAPSHOT-bin.zip, 2019-11-02 DH version
//                "RnFgSc", // nativerl-1.0.0-SNAPSHOT-bin.zip, 2019-11-27 DH version,
                "oyeGse", // nativerl-1.0.0-SNAPSHOT-bin.zip, multi-agent #728
                "fDRBHd"  // OpenJDK8U-jdk_x64_linux_hotspot_8u222b10.tar.gz
        ));
        vTable.put(PathmindHelper.VERSION_0_0_24, Arrays.asList(
//                "kuQJAd", // PathmindPolicy.jar, 2019-08-28
                "CaYsDe" // PathmindPolicy.jar, multi-agent #728
        ));

        versions.put("PROD", vTable);
    }

    public List<FileReference> getFiles(VersionEnum version) {
        return versions.get(currentMode).getOrDefault(version, List.of())
                .stream()
                .map(it -> new FileReference(it, false))
                .collect(Collectors.toList());
    }


}
