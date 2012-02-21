package org.rustlang.oxide.common;

import java.util.Collection;
import com.google.common.collect.ObjectArrays;

public final class Collections3 {
    private Collections3() {
        // Cannot instantiate class.
    }

    public static <T> T[] toArray(final Collection<T> collection,
            final Class<T> type) {
        final T[] array = ObjectArrays.newArray(type, collection.size());
        return collection.toArray(array);
    }
}
