package org.rustlang.oxide.preferences;

import com.google.inject.Inject;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class RustSyntaxColoringPreferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage {
    private final IPreferenceStore preferenceStore;

    @Inject
    RustSyntaxColoringPreferencePage(final IPreferenceStore preferenceStore) {
        super(FieldEditorPreferencePage.GRID);
        this.preferenceStore = preferenceStore;
    }

    @Override
    public void init(@SuppressWarnings("unused") final IWorkbench workbench) {
        setPreferenceStore(preferenceStore);
        setDescription("Rust syntax coloring preferences.");
    }

    @Override
    protected void createFieldEditors() {
        // TODO: implement.
    }
}
