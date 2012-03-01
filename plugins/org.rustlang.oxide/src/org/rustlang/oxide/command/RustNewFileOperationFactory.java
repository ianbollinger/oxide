package org.rustlang.oxide.command;

import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.rustlang.oxide.model.RustSourceFile;

public interface RustNewFileOperationFactory {
    RustNewFileOperation create(RustSourceFile rustSourceFile,
            IWorkbench workbench, Shell shell,
            IRunnableContext runnableContext);
}
