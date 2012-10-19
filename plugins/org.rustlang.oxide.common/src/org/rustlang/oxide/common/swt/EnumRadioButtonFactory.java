package org.rustlang.oxide.common.swt;

import javax.annotation.concurrent.Immutable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

/**
 * TODO: Document class.
 *
 * @param <T>
 */
@Immutable
public class EnumRadioButtonFactory<T extends Enum<T>> {
    EnumRadioButtonFactory() {
    }

    /**
     * TODO: Document method.
     *
     * @param parent
     * @param value
     * @return
     */
    public Button create(final EnumRadioGroup<T> parent, final T value) {
        // TODO: create factory.
        final Button result = new Button(parent, SWT.RADIO);
        result.setText(value.toString());
        result.setFont(parent.getFont());
        result.addSelectionListener(
                // TODO: create factory.
                new EnumRadioButtonSelectionAdapter<>(parent, value));
        return result;
    }
}
