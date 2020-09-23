package io.skymind.pathmind.shared.data.user;

public interface DeepCloneableInterface<T> {

    <T> T shallowClone();

    default <T> T deepClone() {
        return shallowClone();
    }
}
