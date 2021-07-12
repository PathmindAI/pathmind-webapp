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
import org.apache.commons.lang3.tuple.Pair;

public class AWSFileManager {
    private static AWSFileManager instance;
    private static final String S3_COPY = "aws s3 cp s3://";
    private static final String STATIC_BUCKET = "${S3_BUCKET_STATIC}";

    public static AWSFileManager getInstance() {
        if (instance == null) {
            instance = new AWSFileManager();
        }

        return instance;
    }

    private AWSFileManager() {
        setData();
    }

    private static final Map<VersionEnum, List<String>> versions = new HashMap<>();

    private void setData() {
        Arrays.stream(AnyLogic.values())
                .forEach(v -> versions.put(v, v.convertPath()));
        Arrays.stream(Conda.values())
                .forEach(v -> versions.put(v, v.convertPath()));
        Arrays.stream(JDK.values())
                .forEach(v -> versions.put(v, v.convertPath()));
        Arrays.stream(NativeRL.values())
                .forEach(v -> versions.put(v, v.convertPath()));
        Arrays.stream(PathmindHelper.values())
                .forEach(v -> versions.put(v, v.convertPath()));
    }

    public List<String> getFiles(VersionEnum version) {
        return versions.getOrDefault(version, List.of())
                .stream()
                .map(it -> buildS3CopyCmd(STATIC_BUCKET, it, it))
                .collect(Collectors.toList());
    }

    public Pair<String, String> libFilePathsWithFileName(VersionEnum version) {
        return versions.getOrDefault(version, List.of())
            .stream()
            .map(path -> Pair.of(
                path,
                version instanceof NativeRL ? NativeRL.simpleFileName : new File(path).getName()))
            .findFirst()
            .orElse(null);
    }

    public String buildS3CopyCmd(String bucketName, String filePath, String fileName) {
        return S3_COPY + bucketName + "/" + filePath + " " + new File(fileName).getName() + " > /dev/null";
    }
}
