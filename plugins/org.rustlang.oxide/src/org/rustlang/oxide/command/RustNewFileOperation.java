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
                    shell);
            // TODO: thread containment violation.
            runnableContext.run(true, true, op);
            editorOpener.open(file, workbench);
            return Status.createOkStatus();
        } catch (final InterruptedException e) {
            return Status.createErrorStatus(e);
        } catch (final InvocationTargetException e) {
            return Status.createErrorStatus(e);
        } catch (final PartInitException e) {
            return Status.createErrorStatus(e);
        }
    }

    private IFile getFileHandle() {
        final Path folderName = model.getFolder().getContent();
        final String fileName = model.getName().getContent();
        final Path newFilePath = folderName.append(fileName + ".rs");
        return workspaceRoot.getFile(PathBridge.create(newFilePath));
    }

    public static Status execute(
            @SuppressWarnings("unused") final RustSourceFile model,
            @SuppressWarnings("unused") final ProgressMonitor monitor) {
        return Status.createOkStatus();
    }
}
