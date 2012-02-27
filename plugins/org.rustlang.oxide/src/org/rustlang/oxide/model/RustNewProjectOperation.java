package org.rustlang.oxide.model;

import com.google.common.base.Throwables;
import com.google.common.collect.ObjectArrays;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.sapphire.modeling.ProgressMonitor;
import org.eclipse.sapphire.modeling.Status;
import org.eclipse.sapphire.platform.ProgressMonitorBridge;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.rustlang.oxide.common.SubProgressMonitorFactory;
import org.rustlang.oxide.nature.RustNature;
import org.rustlang.oxide.templates.TemplateFileWriter;
import org.rustlang.oxide.wizards.PerspectiveUpdater;

public class RustNewProjectOperation extends WorkspaceModifyOperation {
    // TODO: provide way of keeping this number in sync.
    private static final int NUMBER_OF_TASKS = 7;
    private final SubProgressMonitorFactory subProgressMonitorFactory;
    private final IProject project;
    private final TemplateFileWriter fileFactory;
    private final IProjectDescription description;
    private final PerspectiveUpdater perspectiveUpdater;
    private final IConfigurationElement configuration;

    @Inject
    RustNewProjectOperation(final IWorkspaceRoot root,
            final SubProgressMonitorFactory subProgressMonitorFactory,
            final PerspectiveUpdater perspectiveUpdater,
            @Assisted final IConfigurationElement configuration,
            @Assisted final IProject project,
            @Assisted final TemplateFileWriter fileFactory,
            @Assisted final IProjectDescription description) {
        super(root);
        this.subProgressMonitorFactory = subProgressMonitorFactory;
        this.project = project;
        this.fileFactory = fileFactory;
        this.description = description;
        this.perspectiveUpdater = perspectiveUpdater;
        this.configuration = configuration;
    }

    /*
     * HACK: this method does nothing and exists to keep Sapphire happy.
     */
    public static final Status execute(
            @SuppressWarnings("unused") final RustProjectOperationModel context,
            @SuppressWarnings("unused") final ProgressMonitor monitor) {
        return Status.createOkStatus();
    }

    public void run(final ProgressMonitor monitor) {
        try {
            run(ProgressMonitorBridge.create(monitor));
        } catch (final Exception e) {
            Throwables.propagate(e);
        }
        perspectiveUpdater.update(configuration);
    }

    @Override
    protected void execute(
            final IProgressMonitor monitor) throws CoreException {
        try {
            monitor.beginTask("Creating Rust project",
                    subProgressMonitorFactory.getWorkScale() * NUMBER_OF_TASKS);
            configureDescription();
            createAndOpenProject(monitor);
            createFiles(description.getName(), monitor);
        } finally {
            monitor.done();
        }
    }

    private void configureDescription() {
        final String[] newNatureIds = ObjectArrays.concat(
                description.getNatureIds(), RustNature.ID);
        description.setNatureIds(newNatureIds);
    }

    private void createAndOpenProject(
            final IProgressMonitor monitor) throws CoreException {
        project.create(subProgressMonitorFactory.create(monitor));
        ensureNotCanceled(monitor);
        project.open(IResource.BACKGROUND_REFRESH,
                subProgressMonitorFactory.create(monitor));
        ensureNotCanceled(monitor);
        project.setDescription(description,
                subProgressMonitorFactory.create(monitor));
        ensureNotCanceled(monitor);
    }

    private void ensureNotCanceled(final IProgressMonitor monitor) {
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
    }

    private void createFiles(final String projectName,
            final IProgressMonitor monitor) throws CoreException {
        createFile("README.md", "readme", monitor);
        createFile("LICENSE.txt", "license", monitor);
        // createFile(".gitignore", "gitignore", monitor);
        createFile(projectName + ".rc", "crate", monitor);
        createFile(projectName + ".rs", "sourcefile", monitor);
    }

    private void createFile(final String fileName, final String templateName,
            final IProgressMonitor monitor) throws CoreException {
        final IFile file = project.getFile(fileName);
        fileFactory.createFile(file, templateName, monitor);
    }
}
