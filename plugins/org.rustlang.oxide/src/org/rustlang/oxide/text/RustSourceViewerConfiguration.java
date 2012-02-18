package org.rustlang.oxide.text;

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

    public RustSourceViewerConfiguration(final RustColorManager colorManager) {
        this.colorManager = colorManager;
    }

    @Override
    public String[] getConfiguredContentTypes(
            @SuppressWarnings("unused") final ISourceViewer sourceViewer) {
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
            final ISourceViewer sourceViewer) {
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
