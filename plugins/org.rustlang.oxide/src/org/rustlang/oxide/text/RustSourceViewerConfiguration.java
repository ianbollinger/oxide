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
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.RuleBasedScanner;
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
            @Nullable final ISourceViewer sourceViewer) {
        // TODO: inject.
        return new String[] {
            IDocument.DEFAULT_CONTENT_TYPE,
            RustPartitions.RUST_DOC,
            RustPartitions.RUST_SINGLE_LINE_COMMENT,
            RustPartitions.RUST_MULTI_LINE_COMMENT,
            RustPartitions.RUST_STRING,
            RustPartitions.RUST_CHARACTER
        };
    }

    @Override
    public IPresentationReconciler getPresentationReconciler(
            final ISourceViewer sourceViewer) {
        // TODO: inject.
        final PresentationReconciler reconciler = new PresentationReconciler();
        reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(
                sourceViewer));
        createDamagerRepairers(reconciler);
        return reconciler;
    }

    private void createDamagerRepairers(
            final PresentationReconciler reconciler) {
        // TODO: extract into a list.
        createDamagerRepairer(reconciler, getCodeScanner(),
                IDocument.DEFAULT_CONTENT_TYPE);
        createDamagerRepairer(reconciler, getRustDocScanner(),
                RustPartitions.RUST_DOC);
        createDamagerRepairer(reconciler, getMultilineCommentScanner(),
                RustPartitions.RUST_MULTI_LINE_COMMENT);
        createDamagerRepairer(reconciler, getSinglelineCommentScanner(),
                RustPartitions.RUST_SINGLE_LINE_COMMENT);
        createDamagerRepairer(reconciler, getStringScanner(),
                RustPartitions.RUST_STRING);
        createDamagerRepairer(reconciler, getStringScanner(),
                RustPartitions.RUST_CHARACTER);
    }

    private void createDamagerRepairer(
            final PresentationReconciler reconciler,
            final ITokenScanner scanner, final String partition) {
        // TODO: create factory.
        final DefaultDamagerRepairer dr = new DefaultDamagerRepairer(
                scanner);
        reconciler.setDamager(dr, partition);
        reconciler.setRepairer(dr, partition);
    }

    private RustCodeScanner getCodeScanner() {
        // TODO: esnure thread safety.
        if (codeScanner == null) {
            // TODO: inject.
            codeScanner = new RustCodeScanner(colorManager);
        }
        return codeScanner;
    }

    private RuleBasedScanner getMultilineCommentScanner() {
        // TODO: inject.
        return new RustCommentScanner(colorManager,
                RustColorConstants.MULTI_LINE_COMMENT);
    }

    private RuleBasedScanner getSinglelineCommentScanner() {
        // TODO: inject.
        return new RustCommentScanner(colorManager,
                RustColorConstants.SINGLE_LINE_COMMENT);
    }

    private RuleBasedScanner getStringScanner() {
        // TODO: inject.
        return new SingleTokenRustScanner(colorManager,
                RustColorConstants.STRING);
    }

    private RuleBasedScanner getRustDocScanner() {
        // TODO: inject.
        return new RustDocScanner(colorManager);
    }
}
