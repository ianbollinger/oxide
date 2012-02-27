package org.rustlang.oxide.model;

import java.lang.reflect.InvocationTargetException;
import com.google.common.collect.ObjectArrays;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.sapphire.modeling.ProgressMonitor;
import org.eclipse.sapphire.platform.ProgressMonitorBridge;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.rustlang.oxide.common.SubProgressMonitorFactory;
import org.rustlang.oxide.nature.RustNature;
import org.rustlang.oxide.templates.TemplateFileWriter;

public class RustNewProjectOperation extends WorkspaceModifyOperation {
    // TODO: provide way of keeping this number in sync.
    private static final int NUMBER_OF_TASKS = 8;
    private final SubProgressMonitorFactory subProgressMonitorFactory;
    private final IProject project;
    private final TemplateFileWriter fileFactory;
    private final IProjectDescription description;

    @Inject
    RustNewProjectOperation(final IWorkspaceRoot root,
            final SubProgressMonitorFactory subProgressMonitorFactory,
            @Assisted final IProject project,
            @Assisted final TemplateFileWriter fileFactory,
            @Assisted final IProjectDescription description) {
        super(root);
        this.subProgressMonitorFactory = subProgressMonitorFactory;
        this.project = project;
        this.fileFactory = fileFactory;
        this.description = description;
    }

    public void run(final ProgressMonitor monitor)
            throws InvocationTargetException, InterruptedException {
        // TODO: remove static call.
        run(ProgressMonitorBridge.create(monitor));
    }

    @Override
    protected void execute(
            final IProgressMonitor monitor) throws CoreException {
        try {
            monitor.beginTask("Creating Rust project",
                    subProgressMonitorFactory.getWorkScale() * NUMBER_OF_TASKS);
            configureDescription();
            ceateAndOpenProject(monitor);
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

    private void ceateAndOpenProject(
            final IProgressMonitor monitor) throws CoreException {
        // TODO: refactor.
        project.create(subProgressMonitorFactory.create(monitor));
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
        project.open(IResource.BACKGROUND_REFRESH,
                subProgressMonitorFactory.create(monitor));
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
        project.setDescription(description,
                subProgressMonitorFactory.create(monitor));
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
    }

    private void createFiles(final String projectName,
            final IProgressMonitor monitor) throws CoreException {
        // TODO: handle monitor appropriately here.
        createFile("README.md", "readme", monitor);
        createFile("LICENSE.txt", "license", monitor);
        // createFile(".gitignore", "gitignore", monitor);
        createFile(projectName + ".rc", "crate", monitor);
        createFile(projectName + ".rs", "sourcefile", monitor);
    }

    private void createFile(final String fileName, final String templateName,
            final IProgressMonitor monitor) throws CoreException {
        fileFactory.createFile(project.getFile(fileName), templateName,
                monitor);
    }
}
