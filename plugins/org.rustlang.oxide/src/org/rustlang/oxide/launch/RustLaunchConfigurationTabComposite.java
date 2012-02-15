package org.rustlang.oxide.launch;

import java.util.List;
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
import org.rustlang.oxide.common.Collections3;
import org.rustlang.oxide.nature.RustNature;

public class RustLaunchConfigurationTabComposite extends Composite {
    private final RustLaunchConfigurationTab tab;
    private final Group projectGroup;
    private final Text projectField;
    private final Button projectBrowseButton;
    private final Group mainGroup;
    private final Text executableField;
    private final Button executableBrowseButton;
    private final Group programArgumentsGroup;
    private final Text programArgumentsTextArea;

    public RustLaunchConfigurationTabComposite(final Composite parent,
            final RustLaunchConfigurationTab tab) {
        super(parent, SWT.NULL);
        this.tab = tab;
        this.projectGroup = createProjectGroup();
        this.mainGroup = createExecutableGroup();
        this.executableField = createExcutableField();
        this.executableBrowseButton = createExecutableBrowseButton();
        this.programArgumentsGroup = createProgramArgumentsGroup();
        this.programArgumentsTextArea = createProgramArgumentsTextArea();
        this.projectField = createProjectField();
        this.projectBrowseButton = createProjectBrowseButton();
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
        text.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(final ModifyEvent event) {
                updateState();
            }
        });
        return text;
    }

    private Text createExcutableField() {
        final Text text = new Text(mainGroup, SWT.BORDER);
        text.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(final ModifyEvent event) {
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
            public void widgetSelected(final SelectionEvent event) {
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
            public void widgetSelected(final SelectionEvent event) {
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
                    final ResourceListSelectionDialog dialog = new ResourceListSelectionDialog(
                            getShell(), Collections3.toArray(rustProjects,
                                    IProject.class));
                    dialog.setTitle("Project Selection");
                    dialog.open();
                    final Object[] objects = dialog.getResult();
                    if (objects != null && objects.length > 0) {
                        projectField.setText(((IProject) objects[0]).getName());
                    }
                } catch (final CoreException e) {
                    OxidePlugin.getLogger().log(e);
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
            public void modifyText(final ModifyEvent event) {
                updateState();
            }
        });
        return text;
    }

    // domain logic -----------------------------------------------------------

    public Text getProjectField() {
        return projectField;
    }

    public void setProject(final String projectname) {
        projectField.setText(projectname);
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
        final String result = programArgumentsTextArea.getText().replace("\n",
                " ");
        return result.replace("\r", " ");
    }

    private void updateState() {
        // tab.validate();
        tab.getLaunchConfigurationDialog().updateButtons();
        tab.getLaunchConfigurationDialog().updateMessage();
    }
}
