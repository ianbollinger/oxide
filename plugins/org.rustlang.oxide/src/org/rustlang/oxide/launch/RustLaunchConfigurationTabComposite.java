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

import javax.annotation.Nullable;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
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
import org.rustlang.oxide.OxidePlugin;
import org.slf4j.Logger;

/**
 * TODO: Document class.
 */
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
            public void modifyText(@Nullable final ModifyEvent event) {
                updateState();
            }
        });
        return text;
    }

    private Text createExcutableField() {
        final Text text = new Text(mainGroup, SWT.BORDER);
        text.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(@Nullable final ModifyEvent event) {
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
            public void widgetSelected(@Nullable final SelectionEvent event) {
                // TODO: browse for the executable.
            }
        });
        return button;
    }

    // TODO: create factory.
    private Button createProjectBrowseButton() {
        final Button button = new Button(projectGroup, SWT.NONE);
        button.setText(BROWSE_MESSAGE);
        final IWorkspaceRoot workspaceRoot =
                ResourcesPlugin.getWorkspace().getRoot();
        final Logger logger = OxidePlugin.getLogger();
        button.addSelectionListener(new ProjectSelectionAdapter(
                getShell(), projectField, workspaceRoot,
                new ProjectListSelectionDialogFactory(), logger));
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
            public void modifyText(@Nullable final ModifyEvent event) {
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
        // FIXME: this certainly is wrong.
        return programArgumentsTextArea.getText().replace("\n", space).replace(
                "\r", space);
    }

    final void updateState() {
        tab.update();
    }
}
