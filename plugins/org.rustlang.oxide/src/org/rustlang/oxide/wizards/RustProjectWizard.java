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

package org.rustlang.oxide.wizards;

import java.lang.reflect.InvocationTargetException;
import javax.annotation.Nullable;
import com.google.inject.Inject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectReferencePage;
import org.rustlang.oxide.command.RustNewProjectOperationFactory;
import org.rustlang.oxide.common.EclipseLogger;

public class RustProjectWizard extends AbstractNewWizard {
    public static final String ID =
            "org.rustlang.oxide.wizards.RustProjectWizard";
    private RustProjectWizardPage projectPage;
    private final IWorkspaceRoot workspaceRoot;
    private final WizardNewProjectReferencePage referencePage;
    private final RustProjectWizardPageFactory projectPageFactory;
    private final ImageDescriptor imageDescriptor;
    private final RustNewProjectOperationFactory projectOperationFactory;
    private final EclipseLogger logger;

    @Inject
    RustProjectWizard(final WizardNewProjectReferencePage referencePage,
            final RustProjectWizardPageFactory projectPageFactory,
            final IWorkspaceRoot workspaceRoot,
            @Nullable final ImageDescriptor imageDescriptor,
            final RustNewProjectOperationFactory projectOperationFactory,
            final EclipseLogger logger) {
        this.referencePage = referencePage;
        this.projectPageFactory = projectPageFactory;
        this.workspaceRoot = workspaceRoot;
        this.imageDescriptor = imageDescriptor;
        this.projectOperationFactory = projectOperationFactory;
        this.logger = logger;
    }

    @Override
    public void init(final IWorkbench workbench,
            final IStructuredSelection selection) {
        this.projectPage = projectPageFactory.create(selection);
        setDefaultPageImageDescriptor(imageDescriptor);
    }

    @Override
    public void addPages() {
        addPage(projectPage);
        if (projectsInWorkspace()) {
            addPage(referencePage);
        }
    }

    private boolean projectsInWorkspace() {
        return workspaceRoot.getProjects().length > 0;
    }

    @Override
    public boolean performFinish() {
        final IProgressMonitor monitor = null;
        try {
            projectOperationFactory.create(projectPage.provideModel(),
                    getConfiguration()).run(monitor);
        } catch (final InterruptedException | InvocationTargetException e) {
            logger.error(e.getMessage(), e);
        }
        return true;
    }
}
