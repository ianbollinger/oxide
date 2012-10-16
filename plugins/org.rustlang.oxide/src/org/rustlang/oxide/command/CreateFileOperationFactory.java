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

package org.rustlang.oxide.command;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ide.undo.CreateFileOperation;
import org.rustlang.oxide.common.WorkspaceUndoAdapterFactory;

public class CreateFileOperationFactory {
    private final WorkspaceUndoAdapterFactory undoFactory;
    private final UndoableOperationWithProgressFactory undoableFactory;

    @Inject
    CreateFileOperationFactory(final WorkspaceUndoAdapterFactory undoFactory,
            final UndoableOperationWithProgressFactory undoableFactory) {
        this.undoableFactory = undoableFactory;
        this.undoFactory = undoFactory;
    }

    public UndoableOperationWithProgress create(final IFile file,
            final Shell shell) {
        final CreateFileOperation operation = provideCreateFileOperation(file);
        final IAdaptable uiInfoAdapter = undoFactory.create(shell);
        return undoableFactory.create(operation, uiInfoAdapter);
    }

    @VisibleForTesting
    CreateFileOperation provideCreateFileOperation(final IFile file) {
        return new CreateFileOperation(file, null, null, "create new file");
    }
}
