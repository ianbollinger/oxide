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

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.rustlang.oxide.text.RustPartitionScanner;
import org.rustlang.oxide.text.RustPartitions;

/**
 * TODO: Document class.
 */
public class RustDocumentSetupParticipant implements IDocumentSetupParticipant {
    @Override
    public void setup(final IDocument document) {
        final IDocumentPartitioner partitioner = createDocumentPartitioner();
        if (document instanceof IDocumentExtension3) {
            ((IDocumentExtension3) document).setDocumentPartitioner(
                    RustPartitions.RUST_PARTITIONING, partitioner);
        } else {
            document.setDocumentPartitioner(partitioner);
        }
        partitioner.connect(document);
    }

    private FastPartitioner createDocumentPartitioner() {
        // TODO: inject or use factory.
        return new FastPartitioner(new RustPartitionScanner(),
                RustPartitions.CONTENT_TYPES);
    }
}
