package org.rustlang.oxide.wizards;

import com.google.inject.Inject;
import org.eclipse.sapphire.modeling.ProgressMonitor;
import org.eclipse.sapphire.modeling.Status;
import org.rustlang.oxide.command.RustNewProjectOperationFactory;
import org.rustlang.oxide.model.RustModelModule.ProjectDefinition;
import org.rustlang.oxide.model.RustProjectOperationModel;

public class RustProjectWizard extends
        AbstractNewWizard<RustProjectOperationModel> {
    private final RustNewProjectOperationFactory projectOperationFactory;

    @Inject
    RustProjectWizard(final RustProjectOperationModel element,
            final RustNewProjectOperationFactory projectOperationFactory,
            @ProjectDefinition final String definition) {
        super(element, definition);
        this.projectOperationFactory = projectOperationFactory;
    }

    @Override
    protected Status performFinish(final ProgressMonitor monitor) {
        // TODO: return real status.
        projectOperationFactory.create(getModelElement().getProject(),
                getConfiguration()).run(monitor);
        return super.performFinish(monitor);
    }
}
