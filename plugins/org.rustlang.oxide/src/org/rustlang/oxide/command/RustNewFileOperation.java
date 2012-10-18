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

import java.lang.reflect.InvocationTargetException;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.rustlang.oxide.common.EditorOpener;
import org.rustlang.oxide.model.RustSourceFile;

public class RustNewFileOperation {
    private final IWorkspaceRoot workspaceRoot;
    private final EditorOpener editorOpener;
    private final CreateFileOperationFactory operationFactory;
    private final RustSourceFile model;
    private final IWorkbench workbench;
    private final Shell shell;
    private final IRunnableContext runnableContext;

    @Inject
    RustNewFileOperation(final IWorkspaceRoot workspaceRoot,
            final EditorOpener editorOpener,
            final CreateFileOperationFactory operationFactory,
            @Assisted final RustSourceFile model,
            @Assisted final IWorkbench workbench, @Assisted final Shell shell,
            @Assisted final IRunnableContext runnableContext) {
        this.workspaceRoot = workspaceRoot;
        this.editorOpener = editorOpener;
        this.operationFactory = operationFactory;
        this.model = model;
        this.workbench = workbench;
        this.shell = shell;
        this.runnableContext = runnableContext;
    }

    public boolean run() {
        try {
            // FIXME: thread containment violation.
            runnableContext.run(true, true, createOperation());
            editorOpener.open(getFileHandle(), getWorkbench());
            return true;
        } catch (final InterruptedException | InvocationTargetException |
                PartInitException e) {
            return false;
        }
    }

    private UndoableOperationWithProgress createOperation() {
        return operationFactory.create(getFileHandle(), getShell());
    }

    private IFile getFileHandle() {
        final String fileName = model.getName();
        final IPath newFilePath = getFolderName().append(fileName + ".rs");
        return workspaceRoot.getFile(newFilePath);
    }

    private IPath getFolderName() {
        return model.getFolder();
    }

    private Shell getShell() {
        return shell;
    }

    private IWorkbench getWorkbench() {
        return workbench;
    }
}
