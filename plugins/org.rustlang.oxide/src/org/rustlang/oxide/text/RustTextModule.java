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
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.rustlang.oxide.text.RustSourceViewerConfiguration.ContentTypes;

/**
 * TODO: Document class.
 */
public class RustTextModule extends AbstractModule {
    @Override
    protected void configure() {
    }

    @Provides
    FastPartitioner provideFastPartitioner(final RustPartitionScanner scanner) {
        return new FastPartitioner(scanner, RustPartitions.CONTENT_TYPES);
    }

    @Provides @ContentTypes
    Iterable<String> provideRustContentTypes() {
        return ImmutableList.of(
                IDocument.DEFAULT_CONTENT_TYPE,
                RustPartitions.RUST_DOC,
                RustPartitions.RUST_SINGLE_LINE_COMMENT,
                RustPartitions.RUST_MULTI_LINE_COMMENT,
                RustPartitions.RUST_STRING,
                RustPartitions.RUST_CHARACTER);
    }

    @Provides @MultiLineComment
    RustCommentScanner provideMultiLineCommentScanner(
            final RustColorManager colorManager) {
        return new RustCommentScanner(colorManager,
                RustColorConstants.MULTI_LINE_COMMENT);
    }

    @Provides @SingleLineComment
    RustCommentScanner provideSingleLineCommentScanner(
            final RustColorManager colorManager) {
        return new RustCommentScanner(colorManager,
                RustColorConstants.SINGLE_LINE_COMMENT);
    }

    @Provides
    SingleTokenRustScanner provideStringScanner(
            final RustColorManager colorManager) {
        return new SingleTokenRustScanner(colorManager,
                RustColorConstants.STRING);
    }

    // TODO: separate into provider class.
    @Provides
    PresentationReconciler providePresentationReconciler(
            final RustCodeScanner codeScanner,
            final RustDocScanner rustDocScanner,
            @MultiLineComment final RustCommentScanner multiLineCommentScanner,
            @SingleLineComment
            final RustCommentScanner singleLineCommentScanner,
            final SingleTokenRustScanner stringScanner) {
        final PresentationReconciler reconciler = new PresentationReconciler();
        // TODO: extract into a list.
        createDamagerRepairer(reconciler, codeScanner,
                IDocument.DEFAULT_CONTENT_TYPE);
        createDamagerRepairer(reconciler, rustDocScanner,
                RustPartitions.RUST_DOC);
        createDamagerRepairer(reconciler, multiLineCommentScanner,
                RustPartitions.RUST_MULTI_LINE_COMMENT);
        createDamagerRepairer(reconciler, singleLineCommentScanner,
                RustPartitions.RUST_SINGLE_LINE_COMMENT);
        createDamagerRepairer(reconciler, stringScanner,
                RustPartitions.RUST_STRING);
        createDamagerRepairer(reconciler, stringScanner,
                RustPartitions.RUST_CHARACTER);
        return reconciler;
    }

    private void createDamagerRepairer(
            final PresentationReconciler reconciler,
            final ITokenScanner scanner, final String partition) {
        final DefaultDamagerRepairer dr = new DefaultDamagerRepairer(
                scanner);
        reconciler.setDamager(dr, partition);
        reconciler.setRepairer(dr, partition);
    }

    @BindingAnnotation @Target({FIELD, METHOD, PARAMETER}) @Retention(RUNTIME)
    public @interface SingleLineComment {}

    @BindingAnnotation @Target({FIELD, METHOD, PARAMETER}) @Retention(RUNTIME)
    public @interface MultiLineComment {}
}
