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

package org.rustlang.oxide.common.swt;

import java.util.List;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.rustlang.oxide.common.Enums2;

public class EnumRadioGroup<T extends Enum<T>> extends Composite {
    private final List<T> items;
    private final Map<T, EnumRadioButton<T>> buttons;
    private T selection;

    EnumRadioGroup(final Composite parent, final Class<T> enumeration) {
        super(parent, SWT.NONE);
        this.items = Enums2.values(enumeration);
        this.buttons = createButtons();
        setFont(parent.getFont());
        final T first = items.get(0);
        this.selection = first;
        select(first);
        createLayout();
    }

    public static <T extends Enum<T>> EnumRadioGroup<T> of(
            final Composite parent, final Class<T> enumeration) {
        return new EnumRadioGroup<>(parent, enumeration);
    }

    private Map<T, EnumRadioButton<T>> createButtons() {
        final ImmutableMap.Builder<T, EnumRadioButton<T>> builder =
                ImmutableMap.builder();
        for (final T item : items) {
            builder.put(item, EnumRadioButton.of(this, item));
        }
        return builder.build();
    }

    private void select(final T value) {
        buttons.get(value).setSelection(true);
    }

    void setSelection(final T selection) {
        this.selection = selection;
    }

    private void createLayout() {
        GridLayoutFactory.swtDefaults().numColumns(items.size())
                .generateLayout(this);
    }

    public T getSelection() {
        return selection;
    }
}
