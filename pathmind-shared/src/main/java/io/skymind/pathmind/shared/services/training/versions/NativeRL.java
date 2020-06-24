package io.skymind.pathmind.shared.services.training.versions;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum NativeRL implements VersionEnum{
    @Deprecated
    VERSION_0_7_0,
    @Deprecated
    VERSION_0_7_6,
    @Deprecated
    VERSION_0_7_6_PBT,
    @Deprecated
    VERSION_0_7_6_RESUME,
    @Deprecated
    VERSION_0_8_1,
    @Deprecated
    VERSION_1_0_1,
    @Deprecated
    VERSION_1_0_3,
    VERSION_1_0_4,
    VERSION_1_0_5,
    VERSION_1_0_6,
    VERSION_1_0_7;

    @Override
    public List<String> fileNames() {
        return Arrays.asList("nativerl-1.0.0-SNAPSHOT-bin.zip");
    }

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
}
