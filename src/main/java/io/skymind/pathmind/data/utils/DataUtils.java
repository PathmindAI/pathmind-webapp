package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.data.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class DataUtils
{
    public static List<Long> convertToIds(List<? extends Data> data) {
        return data.stream().map(item -> item.getId()).collect(Collectors.toList());
    }
}
