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
