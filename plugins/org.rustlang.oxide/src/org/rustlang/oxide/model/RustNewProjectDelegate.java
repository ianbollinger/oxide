package org.rustlang.oxide.model;

import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.sapphire.modeling.ProgressMonitor;
import org.eclipse.sapphire.modeling.Status;
import org.eclipse.sapphire.platform.ProgressMonitorBridge;
import org.rustlang.oxide.templates.RustTemplateContextFactory;
import org.rustlang.oxide.wizards.PerspectiveUpdater;

public class RustNewProjectDelegate {
    private final RustNewProjectOperationFactory factory;
    private final RustTemplateContextFactory templateContextFactory;
    private final PerspectiveUpdater perspectiveUpdater;
    private final IWorkspaceRoot workspaceRoot;
    private final RustProjectOperationModel element;
    private final IConfigurationElement configuration;
    private final ProgressMonitor monitor;

    @Inject
    RustNewProjectDelegate(final RustNewProjectOperationFactory factory,
            final RustTemplateContextFactory templateContextFactory,
            final PerspectiveUpdater perspectiveUpdater,
            final IWorkspaceRoot workspaceRoot,
            @Assisted final RustProjectOperationModel element,
            @Assisted final IConfigurationElement configuration,
            @Assisted final ProgressMonitor monitor) {
        this.factory = factory;
        this.templateContextFactory = templateContextFactory;
        this.perspectiveUpdater = perspectiveUpdater;
        this.workspaceRoot = workspaceRoot;
        this.element = element;
        this.configuration = configuration;
        this.monitor = monitor;
    }

    /*
     * HACK: this method does nothing and exists to keep Sapphire happy.
     */
    public static final Status execute(
            @SuppressWarnings("unused") final RustProjectOperationModel context,
            @SuppressWarnings("unused") final ProgressMonitor monitor) {
        return Status.createOkStatus();
    }

    public void run() {
        final RustProject modelElement = element.getProject();
        final TemplateContext templateContext = templateContextFactory
                .create(modelElement);
        final String projectName = modelElement.getProjectName().getContent();
        final IProject project = workspaceRoot.getProject(projectName);
        final IRunnableWithProgress operation = factory.create(project,
                templateContext);
        try {
            operation.run(ProgressMonitorBridge.create(monitor));
        } catch (final Exception e) {
            Throwables.propagate(e);
        }
        perspectiveUpdater.update(configuration);
    }
}
