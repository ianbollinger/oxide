package org.rustlang.oxide.model;

import com.google.inject.Inject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.text.templates.TemplateContext;
import org.rustlang.oxide.templates.SapphireTemplateContextFactory;
import org.rustlang.oxide.templates.TemplateFileWriter;
import org.rustlang.oxide.templates.TemplateFileWriterFactory;

public class RustNewProjectOperationFactory {
    private final RustNewProjectOperationInnerFactory factory;
    private final SapphireTemplateContextFactory templateContextFactory;
    private final IWorkspace workspace;
    private final IWorkspaceRoot workspaceRoot;
    private final TemplateFileWriterFactory templateFileWriterFactory;

    @Inject
    RustNewProjectOperationFactory(
            final RustNewProjectOperationInnerFactory factory,
            final SapphireTemplateContextFactory templateContextFactory,
            final IWorkspace workspace,
            final IWorkspaceRoot workspaceRoot,
            final TemplateFileWriterFactory templateFileWriterFactory) {
        this.factory = factory;
        this.templateContextFactory = templateContextFactory;
        this.workspace = workspace;
        this.workspaceRoot = workspaceRoot;
        this.templateFileWriterFactory = templateFileWriterFactory;
    }

    public RustNewProjectOperation create(final RustProject element,
            final IConfigurationElement configuration) {
        final TemplateContext templateContext = templateContextFactory
                .create(RustProject.TYPE, element);
        // TODO: remove LoD violation.
        final String projectName = element.getProjectName().getContent();
        final IProject project = workspaceRoot.getProject(projectName);
        final IProjectDescription description = workspace
                .newProjectDescription(projectName);
        final TemplateFileWriter templateFileFactory =
                templateFileWriterFactory.create(templateContext);
        return factory.create(configuration, project, templateFileFactory,
                description);
    }
}
