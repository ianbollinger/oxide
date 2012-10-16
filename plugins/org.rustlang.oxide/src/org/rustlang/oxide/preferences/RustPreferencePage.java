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

package org.rustlang.oxide.preferences;

import javax.annotation.Nullable;
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
    public void init(
            @SuppressWarnings("unused") @Nullable final IWorkbench workbench) {
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
        final Composite parent = getFieldEditorParent();
        final String string = RustPreferenceKey.COMPILER_PATH.toString();
        assert string != null && parent != null;
        return new LaxFileFieldEditor(string, "Rust &compiler path", parent);
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
