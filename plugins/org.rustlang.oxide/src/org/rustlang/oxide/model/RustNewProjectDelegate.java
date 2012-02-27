package org.rustlang.oxide.model;

import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.sapphire.modeling.ProgressMonitor;
import org.eclipse.sapphire.modeling.Status;
import org.rustlang.oxide.templates.SapphireTemplateContextFactory;
import org.rustlang.oxide.templates.TemplateFileWriter;
import org.rustlang.oxide.templates.TemplateFileWriterFactory;
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
    private final TemplateFileWriterFactory templateFileWriterFactory;

    @Inject
    RustNewProjectDelegate(final RustNewProjectOperationFactory factory,
            final SapphireTemplateContextFactory templateContextFactory,
            final PerspectiveUpdater perspectiveUpdater,
            final IWorkspace workspace,
            final IWorkspaceRoot workspaceRoot,
            final TemplateFileWriterFactory templateFileWriterFactory,
            @Assisted final RustProject element,
            @Assisted final IConfigurationElement configuration,
            @Assisted final ProgressMonitor monitor) {
        this.factory = factory;
        this.templateContextFactory = templateContextFactory;
        this.perspectiveUpdater = perspectiveUpdater;
        this.workspace = workspace;
        this.workspaceRoot = workspaceRoot;
        this.templateFileWriterFactory = templateFileWriterFactory;
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
        final TemplateFileWriter templateFileFactory =
                templateFileWriterFactory.create(templateContext);
        final RustNewProjectOperation operation = factory.create(project,
                templateFileFactory, description);
        try {
            operation.run(monitor);
        } catch (final Exception e) {
            Throwables.propagate(e);
        }
        perspectiveUpdater.update(configuration);
    }
}
