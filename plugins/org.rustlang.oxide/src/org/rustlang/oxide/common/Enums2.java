package org.rustlang.oxide.common;

import java.util.List;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;

public class Enums2 {
    public static <T extends Enum<T>> List<T> values(
            final Class<T> enumeration) {
        final Object result;
        try {
            result = enumeration.getMethod("values").invoke(enumeration);
        } catch (final Throwable t) {
            throw Throwables.propagate(t);
        }
        @SuppressWarnings("unchecked")
        final T[] items = (T[]) result;
        return ImmutableList.copyOf(items);
    }
}
