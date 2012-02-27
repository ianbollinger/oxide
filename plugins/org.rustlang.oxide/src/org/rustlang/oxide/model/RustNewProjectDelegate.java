package org.rustlang.oxide.model;

import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.sapphire.modeling.ProgressMonitor;
import org.eclipse.sapphire.modeling.Status;
import org.eclipse.sapphire.platform.ProgressMonitorBridge;
import org.rustlang.oxide.templates.SapphireTemplateContextFactory;
import org.rustlang.oxide.wizards.PerspectiveUpdater;

public class RustNewProjectDelegate {
    private final RustNewProjectOperationFactory factory;
    private final SapphireTemplateContextFactory templateContextFactory;
    private final PerspectiveUpdater perspectiveUpdater;
    private final IWorkspace workspace;
    private final IWorkspaceRoot workspaceRoot;
    private final RustProject element;
    private final IConfigurationElement configuration;
    private final ProgressMonitor monitor;

    @Inject
    RustNewProjectDelegate(final RustNewProjectOperationFactory factory,
            final SapphireTemplateContextFactory templateContextFactory,
            final PerspectiveUpdater perspectiveUpdater,
            final IWorkspace workspace,
            final IWorkspaceRoot workspaceRoot,
            @Assisted final RustProject element,
            @Assisted final IConfigurationElement configuration,
            @Assisted final ProgressMonitor monitor) {
        this.factory = factory;
        this.templateContextFactory = templateContextFactory;
        this.perspectiveUpdater = perspectiveUpdater;
        this.workspace = workspace;
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
        final TemplateContext templateContext = templateContextFactory
                .create(RustProject.TYPE, element);
        // TODO: remove LoD violation.
        final String projectName = element.getProjectName().getContent();
        final IProject project = workspaceRoot.getProject(projectName);
        final IProjectDescription description = workspace
                .newProjectDescription(projectName);
        final IRunnableWithProgress operation = factory.create(project,
                description, templateContext);
        try {
            operation.run(ProgressMonitorBridge.create(monitor));
        } catch (final Exception e) {
            Throwables.propagate(e);
        }
        perspectiveUpdater.update(configuration);
    }
}
