package org.rustlang.oxide.common;

import com.google.common.collect.ObjectArrays;

public final class ObjectArrays2 {
    private ObjectArrays2() {
        // Cannot instantiate class.
    }

    // TODO: replace with Apache commons implementation.
    public static <T> T[] remove(final T[] array, final int index) {
        final T[] result = ObjectArrays.newArray(array, array.length - 1);
        System.arraycopy(array, 0, result, 0, index);
        System.arraycopy(array, index + 1, result, index, array.length - index
                - 1);
        return result;
    }
}
