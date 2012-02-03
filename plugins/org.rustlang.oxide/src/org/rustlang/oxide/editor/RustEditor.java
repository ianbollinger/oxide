package org.rustlang.oxide.editor;

import org.eclipse.ui.editors.text.TextEditor;
import org.rustlang.oxide.text.RustColorManager;
import org.rustlang.oxide.text.RustSourceViewerConfiguration;

public class RustEditor extends TextEditor {
    private RustColorManager colorManager;

    public RustEditor() {
        super();
        colorManager = new RustColorManager();
        setSourceViewerConfiguration(new RustSourceViewerConfiguration(
                colorManager));
        setDocumentProvider(new RustDocumentProvider());
    }

    @Override
    public void dispose() {
        colorManager.dispose();
        super.dispose();
    }
}
