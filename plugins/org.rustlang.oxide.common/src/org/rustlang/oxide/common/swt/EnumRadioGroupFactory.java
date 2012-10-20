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
import com.google.inject.Inject;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;
import org.rustlang.oxide.common.Enums2;

/**
 * TODO: Document class.
 *
 * @param <T>
 */
public class EnumRadioGroupFactory {
    private final GridLayoutFactory gridLayoutFactory;

    @Inject
    EnumRadioGroupFactory() {
        // TODO: inject field.
        this.gridLayoutFactory = GridLayoutFactory.swtDefaults();
    }

    public <T extends Enum<T>> EnumRadioGroup<T> create(
            final Composite parent, final Class<T> enumeration) {
        final List<T> items = Enums2.values(enumeration);
        final T first = items.get(0);
        final EnumRadioGroup<T> group = new EnumRadioGroup<>(parent,
                items, new EnumRadioButtonFactory<T>(), first);
        group.setFont(parent.getFont());
        group.select(first);
        createLayout(group, items.size());
        return group;
    }

    private void createLayout(final Composite composite, final int columns) {
        gridLayoutFactory.numColumns(columns).generateLayout(composite);
    }
}
