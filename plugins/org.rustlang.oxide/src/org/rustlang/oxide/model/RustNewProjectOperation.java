package org.rustlang.oxide.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.google.common.base.Charsets;
import com.google.common.collect.ObjectArrays;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.rustlang.oxide.OxideLogger;
import org.rustlang.oxide.nature.RustNature;

public class RustNewProjectOperation extends WorkspaceModifyOperation {
    // TODO: provide way of keeping this number in sync.
    private static final int NUMBER_OF_TASKS = 8;
    private static final int WORK_SCALE = 1000;
    private final OxideLogger logger;
    private final IWorkspace workspace;
    private final TemplateStore templateStore;
    private final IProject project;
    private final TemplateContext templateContext;

    @Inject
    RustNewProjectOperation(final OxideLogger logger,
            final IWorkspace workspace, final TemplateStore templateStore,
            @Assisted final IProject project,
            @Assisted final TemplateContext templateContext) {
        this.logger = logger;
        this.workspace = workspace;
        this.templateStore = templateStore;
        this.project = project;
        this.templateContext = templateContext;
    }

    @Override
    protected void execute(
            final IProgressMonitor monitor) throws CoreException {
        try {
            monitor.beginTask("Creating Rust project", WORK_SCALE
                    * NUMBER_OF_TASKS);
            final IProjectDescription description = createProjectDescription();
            ceateAndOpenProject(description, monitor);
            createFiles(description.getName(), monitor);
        } finally {
            monitor.done();
        }
    }

    private IProjectDescription createProjectDescription() {
        final IProjectDescription description = workspace
                .newProjectDescription(project.getName());
        // TODO: handle this correctly.
        // if (location != null) {
        // description.setLocationURI(location);
        // }
        final String[] newNatureIds = ObjectArrays.concat(
                description.getNatureIds(), RustNature.ID);
        description.setNatureIds(newNatureIds);
        return description;
    }

    private void ceateAndOpenProject(final IProjectDescription description,
            final IProgressMonitor monitor) throws CoreException {
        // TODO: refactor.
        project.create(provideSubProgressMonitor(monitor));
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
        project.open(IResource.BACKGROUND_REFRESH,
                provideSubProgressMonitor(monitor));
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
        project.setDescription(description, provideSubProgressMonitor(monitor));
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
    }

    // TODO: make this a factory.
    IProgressMonitor provideSubProgressMonitor(final IProgressMonitor monitor) {
        return new SubProgressMonitor(monitor, WORK_SCALE);
    }

    private void createFiles(final String projectName,
            final IProgressMonitor monitor) throws CoreException {
        try {
            templateStore.load();
        } catch (final IOException e) {
            // TODO: don't ignore exception.
            return;
        }
        // TODO: handle monitor appropriately here.
        createFileFromTemplate("README.md", "readme", monitor);
        createFileFromTemplate("LICENSE.txt", "license", monitor);
        // createFileFromTemplate(".gitignore", "gitignore", monitor);
        createFileFromTemplate(projectName + ".rc", "crate", monitor);
        createFileFromTemplate(projectName + ".rs", "sourcefile", monitor);
    }

    private void createFileFromTemplate(final String fileName,
            final String templateName,
            final IProgressMonitor monitor) throws CoreException {
        final InputStream stream = getTemplateInputStream(templateName);
        final boolean force = false;
        final IProgressMonitor monitor2 = provideSubProgressMonitor(monitor);
        project.getFile(fileName).create(stream, force, monitor2);
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
    }

    private InputStream getTemplateInputStream(final String name) {
        // TODO: replace with findTeplateById
        final Template template = templateStore.findTemplate(name);
        try {
            final TemplateBuffer buffer = templateContext.evaluate(template);
            return new ByteArrayInputStream(buffer.getString().getBytes(
                    Charsets.UTF_8));
        } catch (final BadLocationException e) {
            logger.log(e);
        } catch (final TemplateException e) {
            logger.log(e);
        }
        return new ByteArrayInputStream(new byte[] {});
    }
}
