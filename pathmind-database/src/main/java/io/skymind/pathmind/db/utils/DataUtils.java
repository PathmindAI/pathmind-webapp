package io.skymind.pathmind.db.utils;

import java.util.List;
import java.util.stream.Collectors;

import io.skymind.pathmind.shared.data.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataUtils {
    public static List<Long> convertToIds(List<? extends Data> data) {
        return data.stream().map(item -> item.getId()).collect(Collectors.toList());
    }
}
