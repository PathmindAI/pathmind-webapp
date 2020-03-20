package io.skymind.pathmind.services.training.versions;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.skymind.pathmind.shared.services.training.versions.AnyLogic;
import io.skymind.pathmind.shared.services.training.versions.Conda;
import io.skymind.pathmind.shared.services.training.versions.JDK;
import io.skymind.pathmind.shared.services.training.versions.NativeRL;
import io.skymind.pathmind.shared.services.training.versions.PathmindHelper;
import io.skymind.pathmind.shared.services.training.versions.VersionEnum;

public class AWSFileManager {
    private static AWSFileManager instance;
    private static final String S3_COPY = "aws s3 cp s3://${ENVIRONMENT}-";
    private static final String STATIC_BUCKET = "training-static-files.pathmind.com";
    private static final String DYNAMIC_BUCKET = "training-dynamic-files.pathmind.com";

    public static AWSFileManager getInstance() {
        if (instance == null) {
            instance = new AWSFileManager();
        }

        return instance;
    }

    private AWSFileManager() {
        setData();
    }

    private static final Map<String, Map<VersionEnum, List<String>>> versions = new HashMap<>();
    private String currentMode = "PROD";

    private void setData() {
        Map<VersionEnum, List<String>> vTable = new HashMap<>();
//        vTable.put(AnyLogic.VERSION_8_5_1, Arrays.asList(
//                "baseEnv.zip" // Anylogic 8.5.1 Base Environment: baseEnv.zip
//        ));
//        vTable.put(NativeRL.VERSION_0_7_0, Arrays.asList(
//                "rllibpack.tar.gz",                 // conda TF 1.13.1, RAY 0.7.6
//                "nativerl-1.0.0-SNAPSHOT-bin.zip", // nativerl-1.0.0-SNAPSHOT-bin.zip, 2019-11-27 DH version
//                "OpenJDK8U-jdk_x64_linux_hotspot_8u222b10.tar.gz"  // OpenJDK8U-jdk_x64_linux_hotspot_8u222b10.tar.gz
//        ));
//        vTable.put(PathmindHelper.VERSION_0_0_24, Arrays.asList(
//                "PathmindPolicy.jar" // PathmindPolicy.jar, 2019-08-28
//        ));

        Arrays.stream(AnyLogic.values())
                .forEach(v -> vTable.put(v, v.convertPath()));
        Arrays.stream(Conda.values())
                .forEach(v -> vTable.put(v, v.convertPath()));
        Arrays.stream(JDK.values())
                .forEach(v -> vTable.put(v, v.convertPath()));
        Arrays.stream(NativeRL.values())
                .forEach(v -> vTable.put(v, v.convertPath()));
        Arrays.stream(PathmindHelper.values())
                .forEach(v -> vTable.put(v, v.convertPath()));

        versions.put("PROD", vTable);
    }

    public List<String> getFiles(VersionEnum version) {
        return versions.get(currentMode).getOrDefault(version, List.of())
                .stream()
                .map(it -> buildS3CopyCmd(STATIC_BUCKET, it, it))
                .collect(Collectors.toList());
    }

    private String buildS3CopyCmd(String bucket, String filePath, String fileName) {
        return S3_COPY + bucket + "/" + filePath + " " + new File(fileName).getName();
    }

    public String buildCheckpointCopyCmd(String checkpointPath, String fileName) {
        return S3_COPY + DYNAMIC_BUCKET + "/" + checkpointPath + " " + new File(fileName).getName();
    }
}
