package org.rustlang.oxide.common;

import java.util.List;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;

public final class Enums2 {
    private Enums2() {
        // Cannot instantiate class.
    }

    public static <T extends Enum<T>> List<T> values(
            final Class<T> enumeration) {
        final Object result;
        try {
            result = enumeration.getMethod("values").invoke(enumeration);
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
        @SuppressWarnings("unchecked")
        final T[] items = (T[]) result;
        return ImmutableList.copyOf(items);
    }
}
