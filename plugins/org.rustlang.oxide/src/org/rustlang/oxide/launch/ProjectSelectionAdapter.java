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

package org.rustlang.oxide.launch;

import java.util.List;
import javax.annotation.Nullable;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.rustlang.oxide.common.Loggers;
import org.rustlang.oxide.nature.RustNature;
import org.slf4j.Logger;

/**
 * TODO: Document class.
 */
public class ProjectSelectionAdapter extends SelectionAdapter {
    private final Shell shell;
    private final Text field;
    private final IWorkspaceRoot workspaceRoot;
    private final ProjectListSelectionDialogFactory factory;
    private final Logger logger;

    @Inject
    ProjectSelectionAdapter(final Shell shell, final Text field,
            final IWorkspaceRoot workspaceRoot,
            final ProjectListSelectionDialogFactory factory,
            final Logger logger) {
        this.shell = shell;
        this.field = field;
        this.workspaceRoot = workspaceRoot;
        this.factory = factory;
        this.logger = logger;
    }

    @Override
    public void widgetSelected(@Nullable final SelectionEvent event) {
        try {
            displayDialog();
        } catch (final CoreException e) {
            Loggers.logThrowable(logger, e);
        }
    }

    private void displayDialog() throws CoreException {
        final SelectionDialog dialog = createDialog();
        dialog.open();
        final Object[] objects = dialog.getResult();
        if (objects != null && objects.length > 0) {
            field.setText(((IProject) objects[0]).getName());
        }
    }

    private SelectionDialog createDialog() throws CoreException {
        return factory.create(getRustProjects(), shell);
    }

    private List<IProject> getRustProjects() throws CoreException {
        final ImmutableList.Builder<IProject> rustProjects =
                ImmutableList.builder();
        for (final IProject project : getAllProjects()) {
            if (isOpenRustProject(project)) {
                rustProjects.add(project);
            }
        }
        return rustProjects.build();
    }

    private List<IProject> getAllProjects() {
        return ImmutableList.copyOf(workspaceRoot.getProjects());
    }

    private boolean isOpenRustProject(
            final IProject project) throws CoreException {
        // TODO: avoid static field?
        return project.isOpen() && project.hasNature(RustNature.ID);
    }
}
