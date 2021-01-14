package io.skymind.pathmind.shared.data;

public interface DeepCloneableInterface<T> {

    T shallowClone();

    default T deepClone() {
        return shallowClone();
    }

}
