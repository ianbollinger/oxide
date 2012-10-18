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

import com.google.inject.Inject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.text.templates.TemplateContext;
import org.rustlang.oxide.model.RustProject;
import org.rustlang.oxide.templates.ProjectTemplateContextFactory;
import org.rustlang.oxide.templates.TemplateFileWriter;
import org.rustlang.oxide.templates.TemplateFileWriterFactory;

public class RustNewProjectOperationFactory {
    private final RustNewProjectOperationInnerFactory factory;
    private final ProjectTemplateContextFactory templateContextFactory;
    private final IWorkspace workspace;
    private final IWorkspaceRoot workspaceRoot;
    private final TemplateFileWriterFactory templateFileWriterFactory;

    @Inject
    RustNewProjectOperationFactory(
            final RustNewProjectOperationInnerFactory factory,
            final ProjectTemplateContextFactory templateContextFactory,
            final IWorkspace workspace,
            final IWorkspaceRoot workspaceRoot,
            final TemplateFileWriterFactory templateFileWriterFactory) {
        this.factory = factory;
        this.templateContextFactory = templateContextFactory;
        this.workspace = workspace;
        this.workspaceRoot = workspaceRoot;
        this.templateFileWriterFactory = templateFileWriterFactory;
    }

    public RustNewProjectOperation create(final RustProject rustProject,
            final IConfigurationElement configuration) {
        final String projectName = rustProject.getProjectName();
        final IProject project = workspaceRoot.getProject(projectName);
        final TemplateFileWriter templateFileFactory =
                templateFileWriterFactory.create(
                        createTemplateContext(rustProject));
        return factory.create(configuration, project, templateFileFactory,
                createProjectDescription(projectName));
    }

    private TemplateContext createTemplateContext(final RustProject project) {
        return templateContextFactory.create(project);
    }

    private IProjectDescription createProjectDescription(
            final String projectName) {
        return workspace.newProjectDescription(projectName);
    }
}
