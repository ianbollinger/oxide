package org.rustlang.oxide.editor;

import com.google.inject.Inject;
import org.eclipse.ui.editors.text.TextEditor;
import org.rustlang.oxide.text.RustColorManager;
import org.rustlang.oxide.text.RustSourceViewerConfiguration;

public class RustEditor extends TextEditor {
    public static final String ID =
            "org.rustlang.oxide.editor.RustEditor";
    private final RustColorManager colorManager;

    @Inject
    RustEditor(final RustColorManager colorManager,
            final RustSourceViewerConfiguration configuration,
            final RustDocumentProvider provider) {
        this.colorManager = colorManager;
        setSourceViewerConfiguration(configuration);
        setDocumentProvider(provider);
    }

    @Override
    public void dispose() {
        colorManager.dispose();
        super.dispose();
    }
}
