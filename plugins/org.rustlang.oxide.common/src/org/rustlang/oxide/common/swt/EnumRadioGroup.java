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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * TODO: Document class.
 *
 * @param <T>
 */
public class EnumRadioGroup<T extends Enum<T>> extends Composite {
    private final List<T> items;
    private final EnumRadioButtonFactory<T> factory;
    private final Map<T, Button> buttons;
    private T selection;

    EnumRadioGroup(final Composite parent, final List<T> items,
            final EnumRadioButtonFactory<T> factory,
            final T selection) {
        super(parent, SWT.NONE);
        this.items = items;
        this.factory = factory;
        this.selection = selection;
        // TODO: Don't do work in constructor!
        this.buttons = createButtons();
    }

    // TODO: eliminate circular reference.
    private Map<T, Button> createButtons() {
        final ImmutableMap.Builder<T, Button> builder = ImmutableMap.builder();
        for (final T item : items) {
            builder.put(item, factory.create(this, item));
        }
        return builder.build();
    }

    public T getSelection() {
        return selection;
    }

    void setSelection(final T selection) {
        // TODO: ensure thread-safety.
        this.selection = selection;
    }

    void select(final T value) {
        buttons.get(value).setSelection(true);
    }
}
