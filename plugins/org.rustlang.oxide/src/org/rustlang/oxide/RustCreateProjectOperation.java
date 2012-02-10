package org.rustlang.oxide;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.google.common.base.Charsets;
import com.google.common.collect.ObjectArrays;
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
import org.rustlang.oxide.nature.RustNature;

public class RustCreateProjectOperation extends WorkspaceModifyOperation {
    // TODO: provide way of keeping this number valid.
    private static final int NUMBER_OF_TASKS = 8;
    private static final int WORK_SCALE = 1000;
    private final IProject[] referencedProjects;
    private final IProject project;
    private final IWorkspace workspace;
    private final TemplateStore templateStore;
    private final TemplateContext templateContext;

    public RustCreateProjectOperation(final IProject project,
            final IProject[] referencedProjects, final IWorkspace workspace,
            final TemplateStore templateStore,
            final TemplateContext templateContext) {
        this.referencedProjects = referencedProjects;
        this.project = project;
        this.workspace = workspace;
        this.templateStore = templateStore;
        this.templateContext = templateContext;
    }

    @Override
    protected void execute(final IProgressMonitor monitor) throws CoreException {
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
        if (referencedProjects.length > 0) {
            description.setReferencedProjects(referencedProjects);
        }
        final String[] newNatureIds = ObjectArrays.concat(
                description.getNatureIds(), RustNature.ID);
        description.setNatureIds(newNatureIds);
        return description;
    }

    private void ceateAndOpenProject(final IProjectDescription description,
            final IProgressMonitor monitor) throws CoreException {
        // TODO: refactor.
        project.create(createSubProgressMonitor(monitor));
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
        project.open(IResource.BACKGROUND_REFRESH,
                createSubProgressMonitor(monitor));
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
        project.setDescription(description, createSubProgressMonitor(monitor));
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
    }

    private IProgressMonitor createSubProgressMonitor(
            final IProgressMonitor monitor) {
        return new SubProgressMonitor(monitor, WORK_SCALE);
    }

    private void createFiles(final String projectName,
            final IProgressMonitor monitor) throws CoreException {
        try {
            templateStore.load();
        } catch (IOException e) {
            return;
        }
        // TODO: handle monitor appropriately here.
        createFileFromTemplate("README.md", "readme", monitor);
        createFileFromTemplate("LICENSE.txt", "license", monitor);
        createFileFromTemplate(".gitignore", "gitignore", monitor);
        createFileFromTemplate(projectName + ".rc", "crate", monitor);
        createFileFromTemplate(projectName + ".rs", "sourcefile", monitor);
    }

    private void createFileFromTemplate(final String fileName,
            final String templateName, final IProgressMonitor monitor)
            throws CoreException {
        final InputStream stream = getTemplateInputStream(templateName);
        final boolean force = false;
        final IProgressMonitor monitor2 = createSubProgressMonitor(monitor);
        project.getFile(fileName).create(stream, force, monitor2);
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
    }

    private InputStream getTemplateInputStream(final String name) {
        // TODO: replace with findTeplateById
        final Template template = templateStore.findTemplate(name);
        try {
            // TODO: fill in variables.
            final TemplateBuffer buffer = templateContext.evaluate(template);
            return new ByteArrayInputStream(buffer.getString().getBytes(
                    Charsets.UTF_8));
        } catch (final BadLocationException e) {
            OxidePlugin.log(e);
        } catch (final TemplateException e) {
            OxidePlugin.log(e);
        }
        return new ByteArrayInputStream(new byte[] {});
    }
}
