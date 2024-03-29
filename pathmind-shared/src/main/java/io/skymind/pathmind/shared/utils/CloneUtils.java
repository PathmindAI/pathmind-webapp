package io.skymind.pathmind.shared.utils;

import io.skymind.pathmind.shared.data.DeepCloneableInterface;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CloneUtils {

    public static <T extends DeepCloneableInterface> List<T> shallowCloneList(List<T> originalList) {
        if (originalList == null) {
            return null;
        }
        ArrayList<T> clonedList = new ArrayList<T>(originalList.size());
        originalList.stream().forEach(item -> clonedList.add((T) item.shallowClone()));
        return clonedList;
    }

    public static Map<Integer, Map<Integer, Double>> cloneMapIntegerMapIntegerDouble(Map<Integer, Map<Integer, Double>> originalMap) {
        if (originalMap == null) {
            return null;
        }
        Map<Integer, Map<Integer, Double>> copy = new LinkedHashMap<>();
        originalMap.keySet().stream().forEach(key ->
                copy.put(key, cloneMapIntegerDouble(originalMap.get(key))));
        return copy;
    }

    public static Map<Integer, Double> cloneMapIntegerDouble(Map<Integer, Double> originalMap) {
        if (originalMap == null) {
            return null;
        }
        Map<Integer, Double> copy = new LinkedHashMap<>();
        originalMap.keySet().stream().forEach(key ->
                copy.put(key, originalMap.get(key)));
        return copy;
    }

    public static <T extends DeepCloneableInterface> T shallowClone(T item) {
        if (item == null) {
            return null;
        }
        return (T) item.shallowClone();
    }
}
