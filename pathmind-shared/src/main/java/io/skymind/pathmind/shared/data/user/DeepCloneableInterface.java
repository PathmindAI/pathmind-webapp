package io.skymind.pathmind.shared.data.user;

public interface DeepCloneableInterface<T> {

    T shallowClone();

    default T deepClone() {
        return shallowClone();
    }

}
