/*
 * Copyright 2012 Ian D. Bollinger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.rustlang.oxide.common;

import java.util.List;
import javax.annotation.concurrent.Immutable;
import com.google.common.collect.ImmutableList;

/**
 * Static utility methods pertaining to enumerations.
 */
@Immutable
public final class Enums2 {
    private Enums2() {
    }

    /**
     * Invoke the {@link values} method on the given {@link Enum} class.
     *
     * @param enumeration
     *            the Enum class
     * @return
     */
    public static <T extends Enum<T>> List<T> values(
            final Class<T> enumeration) {
        final T[] items = Reflection2.invokeStaticMethod(enumeration, "values");
        return ImmutableList.copyOf(items);
    }
}
