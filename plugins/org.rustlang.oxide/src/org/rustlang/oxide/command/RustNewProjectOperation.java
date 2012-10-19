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

import com.google.common.collect.ObjectArrays;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.rustlang.oxide.common.SubProgressMonitorFactory;
import org.rustlang.oxide.common.template.TemplateFileWriter;
import org.rustlang.oxide.common.wizard.PerspectiveUpdater;
import org.rustlang.oxide.nature.RustNature;

/**
 * TODO: Document class.
 */
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
            monitor.beginTask("Creating Rust project", calculateTotalWork());
            task(monitor);
        } finally {
            monitor.done();
        }
    }

    private int calculateTotalWork() {
        return subProgressMonitorFactory.getWorkScale() * NUMBER_OF_TASKS;
    }

    private void task(final IProgressMonitor monitor) throws CoreException {
        configureDescription();
        createAndOpenProject(monitor);
        createFiles(monitor);
        perspectiveUpdater.update(configuration);
    }

    private void configureDescription() {
        description.setNatureIds(getNatureIds());
    }

    private String[] getNatureIds() {
        return ObjectArrays.concat(description.getNatureIds(), RustNature.ID);
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

    // TODO: move into common as static method.
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
        final String projectName = description.getName();
        createFile(projectName + ".rc", "crate", monitor);
        createFile(projectName + ".rs", "sourcefile", monitor);
    }

    private void createFile(final String fileName, final String templateName,
            final IProgressMonitor monitor) throws CoreException {
        fileWriter.createFile(project.getFile(fileName), templateName, monitor);
    }
}
