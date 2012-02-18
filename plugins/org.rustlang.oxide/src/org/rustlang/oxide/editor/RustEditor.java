package org.rustlang.oxide.editor;

import org.eclipse.ui.editors.text.TextEditor;
import org.rustlang.oxide.text.RustColorManager;
import org.rustlang.oxide.text.RustSourceViewerConfiguration;

public class RustEditor extends TextEditor {
    public static final String ID =
            "org.rustlang.oxide.editor.RustEditor";
    private final RustColorManager colorManager;

    public RustEditor() {
        super();
        this.colorManager = new RustColorManager();
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
