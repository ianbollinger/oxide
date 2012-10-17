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

package org.rustlang.oxide.text;

import javax.annotation.Nullable;
import com.google.inject.Inject;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public class RustSourceViewerConfiguration extends
        TextSourceViewerConfiguration {
    private RustCodeScanner codeScanner;
    private final RustColorManager colorManager;

    @Inject
    RustSourceViewerConfiguration(final RustColorManager colorManager) {
        this.colorManager = colorManager;
    }

    @Override
    public String[] getConfiguredContentTypes(
            @SuppressWarnings("unused") @Nullable
            final ISourceViewer sourceViewer) {
        return new String[] {
            IDocument.DEFAULT_CONTENT_TYPE,
            RustPartitions.RUST_SINGLE_LINE_COMMENT,
            RustPartitions.RUST_MULTI_LINE_COMMENT,
            RustPartitions.RUST_STRING,
            RustPartitions.RUST_CHARACTER
        };
    }

    @Override
    public IPresentationReconciler getPresentationReconciler(
            @SuppressWarnings("null") final ISourceViewer sourceViewer) {
        // TODO: inject.
        final PresentationReconciler reconciler = new PresentationReconciler();
        reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(
                sourceViewer));
        final DefaultDamagerRepairer dr = new DefaultDamagerRepairer(
                getCodeScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
        return reconciler;
    }

    /*
    @Override
    public IAutoEditStrategy[] getAutoEditStrategies(
            final ISourceViewer sourceViewer, final String contentType) {
        return new IAutoEditStrategy[] { getAutoIndentStrategy() };
    }

    public IAutoEditStrategy getAutoIndentStrategy() {
        if (autoIndentStrategy == null) {
            autoIndentStrategy = new RustAutoIndentStrategy();
        }
        return autoIndentStrategy;
    }
    */

    private RustCodeScanner getCodeScanner() {
        if (codeScanner == null) {
            codeScanner = new RustCodeScanner(colorManager);
            codeScanner.setDefaultReturnToken(new Token(new TextAttribute(
                    colorManager.getColor(RustColorConstants.DEFAULT))));
        }
        return codeScanner;
    }
}
