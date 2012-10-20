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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.annotation.Nullable;
import com.google.common.collect.Iterables;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

// TODO: can we make this class completely generic?
/**
 * TODO: Document class.
 */
public class RustSourceViewerConfiguration
        extends TextSourceViewerConfiguration {
    private final PresentationReconciler presentationReconciler;
    private final Iterable<String> contentTypes;

    @Inject
    RustSourceViewerConfiguration(
            final PresentationReconciler presentationReconciler,
            @ContentTypes final Iterable<String> contentTypes) {
        this.presentationReconciler = presentationReconciler;
        this.contentTypes = contentTypes;
    }

    @Override
    public String[] getConfiguredContentTypes(
            @Nullable final ISourceViewer sourceViewer) {
        return Iterables.toArray(contentTypes, String.class);
    }

    @Override
    public String getConfiguredDocumentPartitioning(
            @Nullable final ISourceViewer sourceViewer) {
        // TODO: inject.
        return RustPartitions.RUST_PARTITIONING;
    }

    @Override
    public IPresentationReconciler getPresentationReconciler(
            @Nullable final ISourceViewer sourceViewer) {
        presentationReconciler.setDocumentPartitioning(
                getConfiguredDocumentPartitioning(sourceViewer));
        return presentationReconciler;
    }

    @BindingAnnotation @Target({FIELD, METHOD, PARAMETER}) @Retention(RUNTIME)
    public @interface ContentTypes {}
}
