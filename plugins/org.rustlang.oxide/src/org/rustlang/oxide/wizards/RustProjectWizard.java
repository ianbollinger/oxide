package org.rustlang.oxide.wizards;

import com.google.inject.Inject;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.sapphire.modeling.ProgressMonitor;
import org.eclipse.sapphire.modeling.Status;
import org.eclipse.sapphire.ui.swt.SapphireWizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.rustlang.oxide.model.RustNewProjectOperationFactory;
import org.rustlang.oxide.model.RustProjectOperationModel;
import org.rustlang.oxide.wizards.RustWizardModule.ProjectDefinition;

public class RustProjectWizard extends SapphireWizard<RustProjectOperationModel>
        implements INewWizard, IExecutableExtension {
    private final RustNewProjectOperationFactory projectOperationFactory;
    private final RustProjectOperationModel element;
    private IConfigurationElement configuration;

    @Inject
    RustProjectWizard(final RustProjectOperationModel element,
            final RustNewProjectOperationFactory projectOperationFactory,
            @ProjectDefinition final String definition) {
        super(element, definition);
        this.element = element;
        this.projectOperationFactory = projectOperationFactory;
    }

    @Override
    protected Status performFinish(final ProgressMonitor monitor) {
        projectOperationFactory.create(element.getProject(),
                configuration).run(monitor);
        return super.performFinish(monitor);
    }

    @Override
    public void init(@SuppressWarnings("unused") final IWorkbench workbench,
            @SuppressWarnings("unused") final IStructuredSelection selection) {
    }

    @Override
    public void setInitializationData(final IConfigurationElement config,
            @SuppressWarnings("unused") final String propertyName,
            @SuppressWarnings("unused") final Object data) {
        this.configuration = config;
    }
}
