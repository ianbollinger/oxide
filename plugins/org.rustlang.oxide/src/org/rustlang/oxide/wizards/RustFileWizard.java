package org.rustlang.oxide.wizards;

import com.google.inject.Inject;
import org.rustlang.oxide.command.RustNewFileOperationFactory;
import org.rustlang.oxide.model.RustModelModule.SourceFileDefinition;
import org.rustlang.oxide.model.RustSourceFile;

public class RustFileWizard extends AbstractNewWizard<RustSourceFile> {
    private final RustNewFileOperationFactory operation;

    @Inject
    RustFileWizard(final RustSourceFile element,
            final RustNewFileOperationFactory operation,
            @SourceFileDefinition final String definition) {
        super(element, definition);
        this.operation = operation;
    }

    @Override
    protected void performPostFinish() {
        // TODO: handle status appropriately.
        operation.create(getModelElement(), getWorkbench(), getShell(),
                getContainer()).run();
    }
}
