package org.rustlang.oxide.wizards;

import com.google.common.base.Throwables;
import com.google.inject.Inject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.sapphire.modeling.ProgressMonitor;
import org.eclipse.sapphire.modeling.Status;
import org.eclipse.sapphire.platform.ProgressMonitorBridge;
import org.eclipse.sapphire.ui.swt.SapphireWizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.rustlang.oxide.model.RustCreateProjectOperationFactory;
import org.rustlang.oxide.model.RustProject;
import org.rustlang.oxide.model.RustProjectOperation;
import org.rustlang.oxide.templates.RustTemplateContextFactory;
import org.rustlang.oxide.wizards.RustWizardModule.ProjectDefinition;

public class RustProjectWizard extends SapphireWizard<RustProjectOperation>
        implements INewWizard, IExecutableExtension {
    private final RustProjectOperation element;
    private final RustCreateProjectOperationFactory projectOperationFactory;
    private final RustTemplateContextFactory templateContextFactory;
    private final PerspectiveUpdater perspectiveUpdater;
    private final IWorkspaceRoot workspaceRoot;
    private IConfigurationElement configuration;

    @Inject
    RustProjectWizard(final RustProjectOperation element,
            final RustCreateProjectOperationFactory projectOperationFactory,
            final RustTemplateContextFactory templateContextFactory,
            final PerspectiveUpdater perspectiveUpdater,
            final IWorkspaceRoot workspaceRoot,
            @ProjectDefinition final String definition) {
        super(element, definition);
        this.element = element;
        this.projectOperationFactory = projectOperationFactory;
        this.templateContextFactory = templateContextFactory;
        this.perspectiveUpdater = perspectiveUpdater;
        this.workspaceRoot = workspaceRoot;
    }

    @Override
    protected Status performFinish(final ProgressMonitor monitor) {
        final RustProject modelElement = element.getProject();
        final TemplateContext templateContext = templateContextFactory
                .create(modelElement);
        final String projectName = modelElement.getProjectName().getContent();
        final IProject project = workspaceRoot.getProject(projectName);
        final IRunnableWithProgress operation = projectOperationFactory.create(
                project, templateContext);
        try {
            operation.run(ProgressMonitorBridge.create(monitor));
        } catch (final Exception e) {
            Throwables.propagate(e);
        }
        perspectiveUpdater.update(configuration);
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
