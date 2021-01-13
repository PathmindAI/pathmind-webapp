package io.skymind.pathmind.shared.services.training.versions;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public enum NativeRL implements VersionEnum {
    @Deprecated
    VERSION_1_0_7,
    VERSION_1_1_0,
    VERSION_1_1_1,
    VERSION_1_2_0,
    VERSION_1_3_0,
    VERSION_1_4_0,
    VERSION_1_4_0_DH;

    private static final String baseFileName = "nativerl-%s-SNAPSHOT-bin.zip";

    private static final EnumSet<NativeRL> OLD_VERSION = EnumSet.of(VERSION_1_0_7, VERSION_1_1_0, VERSION_1_1_1);

    public static List<NativeRL> activeValues() {
        return Arrays.stream(NativeRL.values()).filter(value -> {
            try {
                Field field = NativeRL.class.getField(value.name());
                return !field.isAnnotationPresent(Deprecated.class);
            } catch (NoSuchFieldException | SecurityException e) {
                return false;
            }
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> fileNames() {
        if (OLD_VERSION.contains(this)) {
            return Arrays.asList(String.format(baseFileName, "1.0.0"));
        } else {
            String version = this.toString().replace("VERSION_", "").replace("_", ".");
            return Arrays.asList(String.format(baseFileName, version));
        }
    }
}
