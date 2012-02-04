package org.rustlang.oxide.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.rustlang.oxide.OxidePlugin;

public class RustSyntaxColoringPreferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage {
    public RustSyntaxColoringPreferencePage() {
        super(FieldEditorPreferencePage.GRID);
    }

    @Override
    public void init(IWorkbench workbench) {
        setPreferenceStore(OxidePlugin.getDefault().getPreferenceStore());
        setDescription("Rust editor preferences.");
    }

    @Override
    protected void createFieldEditors() {      
    }
}
