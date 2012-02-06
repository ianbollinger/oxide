package org.rustlang.oxide;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.google.common.base.Charsets;
import com.google.common.collect.ObjectArrays;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
import org.rustlang.oxide.templates.BasicTemplateContext;
import org.rustlang.oxide.templates.RustTemplateContextType;

public final class RustProjects {
    private static final String RUST_NATURE_ID = "org.rustlang.oxide.RustNature";
    private static final String RUST_BUILDER_ID = "org.rustlang.oxide.RustBuilder";

    public static void create(final IProjectDescription description,
            final IProject project, final IProgressMonitor monitor) {
        final int numberOfTasks = 2;
        final int workScale = 1000;
        try {
            monitor.beginTask("Creating Rust project", workScale
                    * numberOfTasks);
            try {
                project.create(description, new SubProgressMonitor(monitor,
                        workScale));
                if (monitor.isCanceled()) {
                    throw new OperationCanceledException();
                }
                project.open(IResource.BACKGROUND_REFRESH,
                        new SubProgressMonitor(monitor, workScale));
                if (monitor.isCanceled()) {
                    throw new OperationCanceledException();
                }
                // TODO: handle monitor appropriately here.
                createFiles(description, project, monitor);
                addNature(description, project, monitor);
                addBuilder(description, project, monitor);
            } catch (final CoreException e) {
                OxidePlugin.log(e);
            }
        } finally {
            monitor.done();
        }
    }

    private static void createFiles(final IProjectDescription description,
            final IProject project, final IProgressMonitor monitor)
            throws CoreException {
        final ContributionTemplateStore store = new ContributionTemplateStore(
                OxidePlugin.getDefault().getPreferenceStore(),
                "RUST_TEMPLATE_STORE");
        try {
            store.load();
        } catch (IOException e) {
        }
        final String projectName = description.getName();
        // TODO: refactor this.
        // TODO: fill in variables.
        createFileFromTemplate(project, store, "README.md", "readme", monitor);
        createFileFromTemplate(project, store, "LICENSE.txt", "license",
                monitor);
        createFileFromTemplate(project, store, ".gitignore", "gitignore",
                monitor);
        createFileFromTemplate(project, store, projectName + ".rc", "crate",
                monitor);
        createFileFromTemplate(project, store, projectName + ".rs",
                "sourcefile", monitor);
    }

    // TODO: too many parameters.
    private static void createFileFromTemplate(final IProject project,
            final TemplateStore store, final String fileName,
            final String templateName, final IProgressMonitor monitor)
            throws CoreException {
        final InputStream stream = getTemplateInputStream(store, templateName);
        project.getFile(fileName).create(stream, false, monitor);
    }

    private static InputStream getTemplateInputStream(
            final TemplateStore templateStore, final String name) {
        try {
            // TODO: replace with findTeplateById
            final Template template = templateStore.findTemplate(name);
            final TemplateContext context = new BasicTemplateContext(
                    new RustTemplateContextType());
            final TemplateBuffer buffer = context.evaluate(template);
            // TODO: handle charset properly.
            return new ByteArrayInputStream(buffer.getString().getBytes(
                    Charsets.UTF_8));
        } catch (final BadLocationException e) {
        } catch (final TemplateException e) {
        }
        return new ByteArrayInputStream(new byte[0]);
    }

    private static void addNature(final IProjectDescription description,
            final IProject project, final IProgressMonitor monitor)
            throws CoreException {
        final String[] newNatureIds = ObjectArrays.concat(
                description.getNatureIds(), RUST_NATURE_ID);
        description.setNatureIds(newNatureIds);
        project.setDescription(description, monitor);
    }

    private static void addBuilder(final IProjectDescription description,
            final IProject project, final IProgressMonitor monitor)
            throws CoreException {
        final ICommand command = description.newCommand();
        command.setBuilderName(RUST_BUILDER_ID);
        final ICommand[] commands = description.getBuildSpec();
        final ICommand[] newCommands = ObjectArrays.concat(commands, command);
        description.setBuildSpec(newCommands);
        project.setDescription(description, monitor);
    }
}
