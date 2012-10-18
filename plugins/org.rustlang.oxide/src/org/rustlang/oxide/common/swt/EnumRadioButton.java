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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

public class EnumRadioButton<T extends Enum<T>> {
    // TODO: eliminate circular dependency if possible.
    private final EnumRadioGroup<T> parent;
    private final T value;
    private final Button button;

    EnumRadioButton(final EnumRadioGroup<T> parent, final T value) {
        this.parent = parent;
        this.value = value;
        this.button = createButton();
    }

    private Button createButton() {
        final Button result = new Button(parent, SWT.RADIO);
        result.setText(value.toString());
        result.setFont(parent.getFont());
        result.addSelectionListener(new EnumRadioButtonSelectionAdapter());
        return result;
    }

    static <T extends Enum<T>> EnumRadioButton<T> of(
            final EnumRadioGroup<T> parent, final T value) {
        return new EnumRadioButton<>(parent, value);
    }

    public void setSelection(final boolean selection) {
        button.setSelection(selection);
    }

    private class EnumRadioButtonSelectionAdapter extends SelectionAdapter {
        @Override
        public void widgetSelected(final SelectionEvent event) {
            parent.setSelection(value);
        }
    }
}
