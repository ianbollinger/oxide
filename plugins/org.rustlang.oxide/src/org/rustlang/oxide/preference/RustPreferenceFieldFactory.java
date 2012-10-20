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

package org.rustlang.oxide.preference;

import javax.annotation.concurrent.Immutable;
import com.google.inject.Inject;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.swt.widgets.Composite;
import org.rustlang.oxide.common.swt.LaxFileFieldEditor;

/**
 * TODO: Document class.
 */
@Immutable
public class RustPreferenceFieldFactory {
    @Inject
    RustPreferenceFieldFactory() {
    }

    /**
     * TODO: Document method.
     *
     * @param parent
     * @return
     */
    public PathEditor createLibraryPathEditor(final Composite parent) {
        return new PathEditor(
                RustPreferenceKey.LIBRARY_PATHS.toString(),
                "Rust &library paths", "Path for Rust libraries",
                parent);
    }

    /**
     * TODO: Document method.
     *
     * @param parent
     * @return
     */
    public FieldEditor createCompilerEditor(final Composite parent) {
        return new LaxFileFieldEditor(
                RustPreferenceKey.COMPILER_PATH.toString(),
                "Rust &compiler path", parent);
    }
}
