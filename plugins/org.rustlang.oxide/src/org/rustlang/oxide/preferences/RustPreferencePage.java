package org.rustlang.oxide.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.rustlang.oxide.OxidePlugin;

public class RustPreferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage {
    public RustPreferencePage() {
        super(FieldEditorPreferencePage.GRID);
    }

    @Override
    public void init(IWorkbench workbench) {
        setPreferenceStore(OxidePlugin.getDefault().getPreferenceStore());
        setDescription("General settings for Rust development.");
    }

    @Override
    protected void createFieldEditors() {
    }
}
