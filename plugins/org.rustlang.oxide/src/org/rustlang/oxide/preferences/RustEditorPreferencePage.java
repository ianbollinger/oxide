package org.rustlang.oxide.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.rustlang.oxide.OxidePlugin;

public class RustEditorPreferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage {
    public RustEditorPreferencePage() {
        super(FieldEditorPreferencePage.GRID);
    }

    @Override
    public void init(@SuppressWarnings("unused") final IWorkbench workbench) {
        setPreferenceStore(OxidePlugin.getDefault().getPreferenceStore());
        setDescription("Rust editor preferences.");
    }

    @Override
    protected void createFieldEditors() {
        // TODO: implement.
    }
}
