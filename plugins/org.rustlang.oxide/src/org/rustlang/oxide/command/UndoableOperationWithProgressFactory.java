package org.rustlang.oxide.command;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;

public interface UndoableOperationWithProgressFactory {
    UndoableOperationWithProgress create(IUndoableOperation operation,
            IAdaptable adaptable);
}
