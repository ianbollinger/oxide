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

package org.rustlang.oxide.editor;

import com.google.inject.Inject;
import org.eclipse.ui.editors.text.TextEditor;
import org.rustlang.oxide.text.RustColorManager;
import org.rustlang.oxide.text.RustSourceViewerConfiguration;

/**
 * TODO: Document class.
 */
public class RustEditor extends TextEditor {
    // TODO: this isn't used anywhere.
    static final String ID = "org.rustlang.oxide.editor.RustEditor";
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
