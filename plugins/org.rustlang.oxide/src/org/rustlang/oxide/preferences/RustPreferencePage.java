package org.rustlang.oxide.preferences;

import com.google.inject.Inject;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class RustPreferencePage extends FieldEditorPreferencePage implements
        IWorkbenchPreferencePage {
    private final IPreferenceStore preferenceStore;

    @Inject
    RustPreferencePage(final IPreferenceStore preferenceStore) {
        super(FieldEditorPreferencePage.GRID);
        this.preferenceStore = preferenceStore;
    }

    @Override
    public void init(@SuppressWarnings("unused") final IWorkbench workbench) {
        setPreferenceStore(preferenceStore);
        setDescription("General settings for Rust development.");
    }

    @Override
    protected void createFieldEditors() {
        addField(provideCompilerEditor());
        addField(provideLibraryPathEditor());
    }

    // TODO: make this a factory.
    PathEditor provideLibraryPathEditor() {
        return new PathEditor(
                RustPreferenceKey.LIBRARY_PATHS.toString(),
                "Rust &library paths", "Path for Rust libraries",
                getFieldEditorParent());
    }

    // TODO: make this a factory.
    LaxFileFieldEditor provideCompilerEditor() {
        return new LaxFileFieldEditor(
                RustPreferenceKey.COMPILER_PATH.toString(),
                "Rust &compiler path", getFieldEditorParent());
    }

    private static final class LaxFileFieldEditor extends FileFieldEditor {
        LaxFileFieldEditor(final String name, final String labelText,
                final Composite parent) {
            super(name, labelText, parent);
        }

        @Override
        public boolean checkState() {
            return true;
        }
    }
}
