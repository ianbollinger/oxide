/*
 * Copyright 2012 Ian D. Bollinger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.rustlang.oxide.command;

import static com.google.common.base.Preconditions.checkNotNull;
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
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.rustlang.oxide.common.PerspectiveUpdater;
import org.rustlang.oxide.common.SubProgressMonitorFactory;
import org.rustlang.oxide.nature.RustNature;
import org.rustlang.oxide.templates.TemplateFileWriter;

public class RustNewProjectOperation extends WorkspaceModifyOperation {
    // TODO: provide way of keeping this number in sync.
    private static final int NUMBER_OF_TASKS = 7;
    private final SubProgressMonitorFactory subProgressMonitorFactory;
    private final PerspectiveUpdater perspectiveUpdater;
    private final IConfigurationElement configuration;
    private final IProject project;
    private final TemplateFileWriter fileWriter;
    private final IProjectDescription description;

    @Inject
    RustNewProjectOperation(final IWorkspaceRoot root,
            final SubProgressMonitorFactory subProgressMonitorFactory,
            final PerspectiveUpdater perspectiveUpdater,
            @Assisted final IConfigurationElement configuration,
            @Assisted final IProject project,
            @Assisted final TemplateFileWriter fileWriter,
            @Assisted final IProjectDescription description) {
        super(root);
        this.subProgressMonitorFactory = subProgressMonitorFactory;
        this.perspectiveUpdater = perspectiveUpdater;
        this.configuration = configuration;
        this.project = project;
        this.fileWriter = fileWriter;
        this.description = description;
    }

    @Override
    protected void execute(
            final IProgressMonitor monitor) throws CoreException {
        try {
            monitor.beginTask("Creating Rust project",
                    subProgressMonitorFactory.getWorkScale() * NUMBER_OF_TASKS);
            task(monitor);
        } finally {
            monitor.done();
        }
    }

    private void task(final IProgressMonitor monitor) throws CoreException {
        configureDescription();
        createAndOpenProject(monitor);
        createFiles(monitor);
        perspectiveUpdater.update(configuration);
    }

    private void configureDescription() {
        final String[] natureIds = description.getNatureIds();
        final String[] newNatureIds = ObjectArrays.concat(natureIds,
                RustNature.ID);
        description.setNatureIds(newNatureIds);
    }

    private void createAndOpenProject(
            final IProgressMonitor monitor) throws CoreException {
        createProject(monitor);
        openProject(monitor);
        setProjectDescription(monitor);
    }

    private void createProject(
            final IProgressMonitor monitor) throws CoreException {
        project.create(subProgressMonitorFactory.create(monitor));
        ensureNotCanceled(monitor);
    }

    private void openProject(
            final IProgressMonitor monitor) throws CoreException {
        project.open(IResource.BACKGROUND_REFRESH,
                subProgressMonitorFactory.create(monitor));
        ensureNotCanceled(monitor);
    }

    private void setProjectDescription(
            final IProgressMonitor monitor) throws CoreException {
        project.setDescription(description,
                subProgressMonitorFactory.create(monitor));
        ensureNotCanceled(monitor);
    }

    private void ensureNotCanceled(final IProgressMonitor monitor) {
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
    }

    private void createFiles(
            final IProgressMonitor monitor) throws CoreException {
        createFile("README.md", "readme", monitor);
        createFile("LICENSE.txt", "license", monitor);
        // createFile(".gitignore", "gitignore", monitor);
        final String projectName = getProjectName();
        createFile(projectName + ".rc", "crate", monitor);
        createFile(projectName + ".rs", "sourcefile", monitor);
    }

    private String getProjectName() {
        return checkNotNull(description.getName());
    }

    private void createFile(final String fileName, final String templateName,
            final IProgressMonitor monitor) throws CoreException {
        final IFile file = checkNotNull(project.getFile(fileName));
        fileWriter.createFile(file, templateName, monitor);
    }
}
