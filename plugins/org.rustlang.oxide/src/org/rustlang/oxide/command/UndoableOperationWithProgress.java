package org.rustlang.oxide.command;

import java.lang.reflect.InvocationTargetException;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

public class UndoableOperationWithProgress implements IRunnableWithProgress {
    private final IUndoableOperation operation;
    private final IAdaptable adaptable;

    @Inject
    UndoableOperationWithProgress(@Assisted final IUndoableOperation operation,
            @Assisted final IAdaptable adaptable) {
        this.operation = operation;
        this.adaptable = adaptable;
    }

    @Override
    public void run(
            final IProgressMonitor monitor) throws InvocationTargetException {
        try {
            operation.execute(monitor, adaptable);
        } catch (final ExecutionException e) {
            throw new InvocationTargetException(e);
        }
    }
}
