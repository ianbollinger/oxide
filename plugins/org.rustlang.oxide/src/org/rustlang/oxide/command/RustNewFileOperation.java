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
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.sapphire.modeling.Path;
import org.eclipse.sapphire.modeling.ProgressMonitor;
import org.eclipse.sapphire.modeling.Status;
import org.eclipse.sapphire.platform.PathBridge;
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

    public Status run() {
        try {
            final IFile file = getFileHandle();
            final IRunnableWithProgress op = operationFactory.create(file,
                    getShell());
            // FIXME: thread containment violation.
            runnableContext.run(true, true, op);
            editorOpener.open(file, getWorkbench());
            final Status status = Status.createOkStatus();
            assert status != null;
            return status;
        } catch (final InterruptedException | InvocationTargetException |
                PartInitException e) {
            final Status status = Status.createErrorStatus(e);
            assert status != null;
            return status;
        }
    }

    private IFile getFileHandle() {
        final Path folderName = model.getFolder().getContent();
        final String fileName = model.getName().getContent();
        final Path newFilePath = folderName.append(fileName + ".rs");
        final IFile file = workspaceRoot.getFile(
                PathBridge.create(newFilePath));
        assert file != null;
        return file;
    }

    public static Status execute(
            @SuppressWarnings("unused") final RustSourceFile model,
            @SuppressWarnings("unused") final ProgressMonitor monitor) {
        final Status status = Status.createOkStatus();
        assert status != null;
        return status;
    }

    @SuppressWarnings("null")
    private Shell getShell() {
        return shell;
    }

    @SuppressWarnings("null")
    private IWorkbench getWorkbench() {
        return workbench;
    }
}
