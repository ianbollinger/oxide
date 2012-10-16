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
import org.rustlang.oxide.OxidePlugin;
import org.rustlang.oxide.nature.RustNature;

public class RustLaunchConfigurationTabComposite extends Composite {
    // TODO: eliminate circular dependency if possible.
    private final RustLaunchConfigurationTab tab;
    private final Group projectGroup;
    private final Text projectField;
    private final Group mainGroup;
    private final Text executableField;
    private final Group programArgumentsGroup;
    private final Text programArgumentsTextArea;

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
        button.setText("Browse...");
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
        button.setText("Browse...");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(
                    @SuppressWarnings("unused") @Nullable
                    final SelectionEvent event) {
                try {
                    final IProject[] projects = ResourcesPlugin.getWorkspace()
                            .getRoot().getProjects();
                    final List<IProject> rustProjects = Lists.newArrayList();
                    for (final IProject project : projects) {
                        if (project.isOpen()
                                && project.hasNature(RustNature.ID)) {
                            rustProjects.add(project);
                        }
                    }
                    final IProject[] array = Iterables.toArray(rustProjects,
                            IProject.class);
                    // TODO: use a factory.
                    final ResourceListSelectionDialog dialog =
                            new ResourceListSelectionDialog(getShell(), array);
                    dialog.setTitle("Project Selection");
                    dialog.open();
                    final Object[] objects = dialog.getResult();
                    if (objects != null && objects.length > 0) {
                        projectField.setText(((IProject) objects[0]).getName());
                    }
                } catch (final CoreException e) {
                    // TODO: inject logger
                    OxidePlugin.getLogger().error(e.getMessage(), e);
                }
            }
        });
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

    @SuppressWarnings("null")
    public Text getProjectField() {
        return projectField;
    }

    public void setProject(final String projectname) {
        projectField.setText(projectname);
    }

    public String getProject() {
        final String text = projectField.getText();
        assert text != null;
        return text;
    }

    public void setExecutable(final String file) {
        executableField.setText(file);
    }

    public String getExecutable() {
        final String text = executableField.getText();
        assert text != null;
        return text;
    }

    public void setProgramArguments(final String args) {
        programArgumentsTextArea.setText(args);
    }

    public String getProgramArguments() {
        final String result = programArgumentsTextArea.getText().replace("\n",
                " ").replace("\r", " ");
        assert result != null;
        return result;
    }

    final void updateState() {
        tab.update();
    }
}
