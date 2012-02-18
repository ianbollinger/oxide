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
        return new EnumRadioButton<T>(parent, value);
    }

    public void setSelection(final boolean selection) {
        button.setSelection(selection);
    }

    private class EnumRadioButtonSelectionAdapter extends SelectionAdapter {
        @Override
        public void widgetSelected(
                @SuppressWarnings("unused") final SelectionEvent event) {
            parent.setSelection(value);
        }
    }
}
