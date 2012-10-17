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

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.List;
import javax.annotation.Nullable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ResourceListSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.rustlang.oxide.OxidePlugin;
import org.rustlang.oxide.nature.RustNature;

public class RustLaunchConfigurationTabComposite extends Composite {
    private static final String BROWSE_MESSAGE = "Browse...";
    // TODO: eliminate circular dependency if possible.
    private final RustLaunchConfigurationTab tab;
    private final Group projectGroup;
    private final Text projectField;
    private final Group mainGroup;
    private final Text executableField;
    private final Group programArgumentsGroup;
    private final Text programArgumentsTextArea;

    // TODO: work should never be done in the constructor; especially not this
    // much.
    public RustLaunchConfigurationTabComposite(final Composite parent,
            final RustLaunchConfigurationTab tab) {
        super(parent, SWT.NULL);
        this.tab = tab;
        this.projectGroup = createProjectGroup();
        this.mainGroup = createExecutableGroup();
        this.executableField = createExcutableField();
        createExecutableBrowseButton();
        this.programArgumentsGroup = createProgramArgumentsGroup();
        this.programArgumentsTextArea = createProgramArgumentsTextArea();
        this.projectField = createProjectField();
        createProjectBrowseButton();
        generateLayout();
    }

    // construction -----------------------------------------------------------

    private void generateLayout() {
        final GridLayoutFactory twoColumnLayoutFactory = GridLayoutFactory
                .swtDefaults().numColumns(2);
        twoColumnLayoutFactory.generateLayout(projectGroup);
        twoColumnLayoutFactory.generateLayout(mainGroup);
        final GridLayoutFactory defaultLayoutFactory = GridLayoutFactory
                .swtDefaults();
        defaultLayoutFactory.generateLayout(programArgumentsGroup);
        defaultLayoutFactory.generateLayout(this);
    }

    private Group createProjectGroup() {
        final Group group = new Group(this, SWT.NONE);
        group.setText("Project");
        return group;
    }

    private Group createExecutableGroup() {
        final Group group = new Group(this, SWT.NONE);
        group.setText("Rust executable");
        return group;
    }

    private Text createProjectField() {
        final Text text = new Text(projectGroup, SWT.BORDER);
        // TODO: the listener is repeated.
        text.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(
                    @SuppressWarnings("unused") @Nullable
                    final ModifyEvent event) {
                updateState();
            }
        });
        return text;
    }

    private Text createExcutableField() {
        final Text text = new Text(mainGroup, SWT.BORDER);
        text.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(
                    @SuppressWarnings("unused") @Nullable
                    final ModifyEvent event) {
                updateState();
            }
        });
        return text;
    }

    private Button createExecutableBrowseButton() {
        final Button button = new Button(mainGroup, SWT.NONE);
        button.setText(BROWSE_MESSAGE);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(
                    @SuppressWarnings("unused") @Nullable
                    final SelectionEvent event) {
                // TODO: browse for the executable.
            }
        });
        return button;
    }

    private Button createProjectBrowseButton() {
        final Button button = new Button(projectGroup, SWT.NONE);
        button.setText(BROWSE_MESSAGE);
        button.addSelectionListener(new ProjectSelectionAdapter());
        return button;
    }

    private Group createProgramArgumentsGroup() {
        final Group group = new Group(this, SWT.NONE);
        group.setText("Program arguments");
        return group;
    }

    private Text createProgramArgumentsTextArea() {
        final Text text = new Text(programArgumentsGroup, SWT.MULTI | SWT.WRAP
                | SWT.V_SCROLL | SWT.BORDER);
        text.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(
                    @SuppressWarnings("unused") @Nullable
                    final ModifyEvent event) {
                updateState();
            }
        });
        return text;
    }

    // domain logic -----------------------------------------------------------

    public Text getProjectField() {
        return projectField;
    }

    public void setProject(final String projectName) {
        projectField.setText(projectName);
    }

    public String getProject() {
        return projectField.getText();
    }

    public void setExecutable(final String file) {
        executableField.setText(file);
    }

    public String getExecutable() {
        return executableField.getText();
    }

    public void setProgramArguments(final String args) {
        programArgumentsTextArea.setText(args);
    }

    public String getProgramArguments() {
        final String space = " ";
        // TODO: this certainly is wrong.
        final String result = programArgumentsTextArea.getText().replace("\n",
                space).replace("\r", space);
        return checkNotNull(result);
    }

    final void updateState() {
        tab.update();
    }

    private final class ProjectSelectionAdapter extends SelectionAdapter {
        @Override
        public void widgetSelected(
                @SuppressWarnings("unused") @Nullable
                final SelectionEvent event) {
            try {
                displayDialog();
            } catch (final CoreException e) {
                // TODO: inject logger
                OxidePlugin.getLogger().error(e.getMessage(), e);
            }
        }

        private void displayDialog() throws CoreException {
            // TODO: use a factory.
            final SelectionDialog dialog = createDialog();
            dialog.open();
            final Object[] objects = dialog.getResult();
            if (objects != null && objects.length > 0) {
                projectField.setText(((IProject) objects[0]).getName());
            }
        }

        private SelectionDialog createDialog() throws CoreException {
            final IProject[] array = Iterables.toArray(getRustProjects(),
                    IProject.class);
            final SelectionDialog dialog = new ResourceListSelectionDialog(
                    getShell(), array);
            dialog.setTitle("Project Selection");
            return dialog;
        }

        private List<IProject> getRustProjects() throws CoreException {
            final List<IProject> rustProjects = Lists.newArrayList();
            for (final IProject project : getAllProjects()) {
                if (isOpenRustProject(project)) {
                    rustProjects.add(project);
                }
            }
            return rustProjects;
        }

        private IProject[] getAllProjects() {
            // TODO: Inject this.
            return ResourcesPlugin.getWorkspace().getRoot().getProjects();
        }

        private boolean isOpenRustProject(
                final IProject project) throws CoreException {
            return project.isOpen() && project.hasNature(RustNature.ID);
        }
    }
}
