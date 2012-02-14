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

    private EnumRadioGroup(final Composite parent, final Class<T> enumeration) {
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
        return new EnumRadioGroup<T>(parent, enumeration);
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
