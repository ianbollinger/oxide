package org.rustlang.oxide.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.rustlang.oxide.OxidePlugin;

public class RustPreferencePage extends FieldEditorPreferencePage implements
        IWorkbenchPreferencePage {
    public RustPreferencePage() {
        super(FieldEditorPreferencePage.GRID);
    }

    @Override
    public void init(final IWorkbench workbench) {
        setPreferenceStore(OxidePlugin.getDefault().getPreferenceStore());
        setDescription("General settings for Rust development.");
    }

    @Override
    protected void createFieldEditors() {
        final FieldEditor compilerEditor = new LaxFileFieldEditor(
                RustPreferenceKey.COMPILER_PATH.toString(),
                "Rust &compiler path", getFieldEditorParent());
        addField(compilerEditor);
        final FieldEditor libraryPathEditor = new PathEditor(
                RustPreferenceKey.LIBRARY_PATHS.toString(),
                "Rust &library paths", "Path for Rust libraries",
                getFieldEditorParent());
        addField(libraryPathEditor);
    }

    private static final class LaxFileFieldEditor extends FileFieldEditor {

        LaxFileFieldEditor(final String name, final String labelText,
                final Composite parent) {
            super(name, labelText, parent);
        }

        // TODO: this is a temporary hack. It should really check whether
        // the executable is on the Path environment variable and then call
        // super.checkState if not.
        @Override
        public boolean checkState() {
            return true;
        }
    }
}
