package io.skymind.pathmind.shared.utils;

import io.skymind.pathmind.shared.data.user.DeepCloneableInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static List<double[]> cloneListDoubleArrays(List<double[]> originalList) {
        if(originalList == null)
            return null;
        ArrayList<double[]> clonedList = new ArrayList<>(originalList.size());
        originalList.stream().forEach(array ->
                clonedList.add(array == null ? null : Arrays.copyOf(array, array.length)));
        return originalList;
    }

    public static <T extends DeepCloneableInterface> T shallowClone(T item) {
        if(item == null)
            return null;
        return (T)item.shallowClone();
    }
}
