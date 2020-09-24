package io.skymind.pathmind.shared.utils;

import io.skymind.pathmind.shared.data.user.DeepCloneableInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class CloneUtils {

    public static <T extends DeepCloneableInterface> List<T> deepCloneList(List<T> originalList) {
        if(originalList == null)
            return null;
        ArrayList<T> clonedList = new ArrayList<T>(originalList.size());
        originalList.stream().forEach(item -> clonedList.add((T)item.deepClone()));
        return clonedList;
    }

    public static <T extends DeepCloneableInterface> List<T> shallowCloneList(List<T> originalList) {
        if(originalList == null)
            return null;
        ArrayList<T> clonedList = new ArrayList<T>(originalList.size());
        originalList.stream().forEach(item -> clonedList.add((T)item.shallowClone()));
        return clonedList;
    }

    public static Map<Integer, Map<Integer, Double>> cloneMapIntegerMapIntegerDouble(Map<Integer, Map<Integer, Double>> originalMap) {
        if(originalMap == null)
            return null;
        Map<Integer, Map<Integer, Double>> clonedMap = new LinkedHashMap<>(originalMap.size());
        Set<Entry<Integer, Map<Integer, Double>>> entries = originalMap.entrySet();
        for (Map.Entry<Integer, Map<Integer, Double>> mapEntry : entries) {
            Set<Entry<Integer, Double>> childMapEntries = mapEntry.getValue().entrySet();
            LinkedHashMap<Integer, Double> childMapShallowCopy = (LinkedHashMap<Integer, Double>) childMapEntries.stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            clonedMap.put(mapEntry.getKey(), childMapShallowCopy);
        }
        return originalMap;
    }

    public static <T extends DeepCloneableInterface> T shallowClone(T item) {
        if(item == null)
            return null;
        return (T)item.shallowClone();
    }
}
