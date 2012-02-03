package org.rustlang.oxide.wizards;

import java.io.File;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class RustProjectWizardPage extends WizardPage {
    private static final int SIZING_TEXT_FIELD_WIDTH = 250;
    private boolean useDefaults = true;
    private final IPath initialLocationFieldValue;
    private String customLocationFieldValue;
    private String initialProjectFieldValue;
    private Text projectNameField;
    private Text locationPathField;
    private Button browseButton;
    private Label locationLabel;
    private final Listener nameModifyListener = new Listener() {
        @Override
        public void handleEvent(@SuppressWarnings("unused") final Event e) {
            setLocationForSelection();
            setPageComplete(validatePage());
        }
    };
    private final Listener locationModifyListener = new Listener() {
        @Override
        public void handleEvent(@SuppressWarnings("unused") final Event e) {
            setPageComplete(validatePage());
        }
    };
    private final SelectionAdapter browseButtonListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(
                @SuppressWarnings("unused") final SelectionEvent event) {
            handleLocationBrowseButtonPressed();
        }
    };

    RustProjectWizardPage(final String pageName) {
        super(pageName);
        setTitle("Rust Project");
        setDescription("Create a new Rust Project.");
        setPageComplete(false);
        initialLocationFieldValue = Platform.getLocation();
        customLocationFieldValue = "";
    }

    @Override
    public void createControl(final Composite parent) {
        final Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite.setFont(parent.getFont());
        createProjectNameGroup(composite);
        createProjectLocationGroup(composite);
        validatePage();
        setErrorMessage(null);
        setMessage(null);
        setControl(composite);
    }

    private void createProjectNameGroup(final Composite parent) {
        final Font font = parent.getFont();
        final Composite projectGroup = new Composite(parent, SWT.NONE);
        final GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        final Label projectLabel = new Label(projectGroup, SWT.NONE);
        projectLabel.setFont(font);
        projectLabel.setText("&Project name:");
        projectNameField = new Text(projectGroup, SWT.BORDER);
        final GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        projectNameField.setLayoutData(data);
        projectNameField.setFont(font);
        if (initialProjectFieldValue != null) {
            projectNameField.setText(initialProjectFieldValue);
        }
        projectNameField.addListener(SWT.Modify, nameModifyListener);
    }

    private void createProjectLocationGroup(final Composite parent) {
        final Font font = parent.getFont();
        final Composite projectGroup = createProjectGroup(parent, font);
        createProjectContentsLabel(font, projectGroup);
        createUseDefaultsButton(font, projectGroup);
        createUserSpecifiedProjectLocationGroup(projectGroup, !useDefaults);
    }

    private void createProjectContentsLabel(final Font font,
            final Composite projectGroup) {
        final GridData labelData = new GridData();
        labelData.horizontalSpan = 3;
        final Label projectContentsLabel = new Label(projectGroup, SWT.NONE);
        projectContentsLabel.setFont(font);
        projectContentsLabel.setText("Project contents:");
        projectContentsLabel.setLayoutData(labelData);
    }

    private void createUseDefaultsButton(final Font font,
            final Composite projectGroup) {
        final GridData buttonData = new GridData();
        buttonData.horizontalSpan = 3;
        final Button useDefaultsButton = new Button(projectGroup, SWT.CHECK
                | SWT.RIGHT);
        final SelectionListener listener = new UseDefaultsListener(
                useDefaultsButton);
        useDefaultsButton.setText("Use &default");
        useDefaultsButton.setSelection(useDefaults);
        useDefaultsButton.setFont(font);
        useDefaultsButton.setLayoutData(buttonData);
        useDefaultsButton.addSelectionListener(listener);
    }

    private Composite createProjectGroup(final Composite parent,
            final Font font) {
        final GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        final Composite projectGroup = new Composite(parent, SWT.NONE);
        projectGroup.setLayout(layout);
        projectGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        projectGroup.setFont(font);
        return projectGroup;
    }

    private void createUserSpecifiedProjectLocationGroup(
            final Composite projectGroup, final boolean enabled) {
        final Font font = projectGroup.getFont();
        createLocationLabel(projectGroup, enabled, font);
        createLocationPathField(projectGroup, enabled, font);
        createBrowseButton(projectGroup, enabled, font);
    }

    private void createLocationLabel(final Composite projectGroup,
            final boolean enabled, final Font font) {
        locationLabel = new Label(projectGroup, SWT.NONE);
        locationLabel.setFont(font);
        locationLabel.setText("Director&y");
        locationLabel.setEnabled(enabled);
    }

    private void createLocationPathField(final Composite projectGroup,
            final boolean enabled, final Font font) {
        final GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        locationPathField = new Text(projectGroup, SWT.BORDER);
        locationPathField.setLayoutData(data);
        locationPathField.setFont(font);
        locationPathField.setEnabled(enabled);
        locationPathField.addListener(SWT.Modify, locationModifyListener);
        if (initialLocationFieldValue != null) {
            locationPathField.setText(initialLocationFieldValue.toOSString());
        }
    }

    private void createBrowseButton(final Composite projectGroup,
            final boolean enabled, final Font font) {
        browseButton = new Button(projectGroup, SWT.PUSH);
        browseButton.setFont(font);
        browseButton.setText("B&rowse");
        browseButton.addSelectionListener(browseButtonListener);
        browseButton.setEnabled(enabled);
    }

    private void handleLocationBrowseButtonPressed() {
        final DirectoryDialog dialog = new DirectoryDialog(
                locationPathField.getShell());
        dialog.setMessage("Select the project contents directory.");
        final String dirName = getProjectLocationFieldValue();
        if (dirName.isEmpty()) {
            final File path = new File(dirName);
            if (path.exists()) {
                dialog.setFilterPath(new Path(dirName).toOSString());
            }
        }
        final String selectedDirectory = dialog.open();
        if (selectedDirectory != null) {
            customLocationFieldValue = selectedDirectory;
            locationPathField.setText(customLocationFieldValue);
        }
    }

    private void setLocationForSelection() {
        if (useDefaults) {
            final IPath defaultPath = Platform.getLocation().append(
                    getProjectNameFieldValue());
            locationPathField.setText(defaultPath.toOSString());
        }
    }

    private boolean validatePage() {
        final IWorkspace workspace = ResourcesPlugin.getWorkspace();
        final String projectFieldContents = getProjectNameFieldValue();
        if (projectFieldContents.isEmpty()) {
            setErrorMessage(null);
            setMessage("Project name is empty");
            return false;
        }
        final IStatus nameStatus = workspace.validateName(projectFieldContents,
                IResource.PROJECT);
        if (!nameStatus.isOK()) {
            setErrorMessage(nameStatus.getMessage());
            return false;
        }
        final String locationFieldContents = getProjectLocationFieldValue();
        if (locationFieldContents.isEmpty()) {
            setErrorMessage(null);
            setMessage("Project location is empty");
            return false;
        }
        final IPath path = new Path("");
        if (!path.isValidPath(locationFieldContents)) {
            setErrorMessage("Project location is not valid");
            return false;
        }
        final IProject projectHandle = getProjectHandle();
        if (projectHandle.exists()) {
            setErrorMessage("Project already exists");
            return false;
        }
        if (isDotProjectFileInLocation()) {
            setErrorMessage(".project found in: "
                    + getLocationPath().toOSString()
                    + " (use import project).");
            return false;
        }
        setErrorMessage(null);
        setMessage(null);
        return true;
    }

    private boolean isDotProjectFileInLocation() {
        IPath path = getLocationPath();
        path = path.append(IProjectDescription.DESCRIPTION_FILE_NAME);
        return path.toFile().exists();
    }

    @Override
    public void setVisible(final boolean visible) {
        super.setVisible(visible);
        if (visible) {
            projectNameField.setFocus();
        }
    }

    public IPath getLocationPath() {
        if (useDefaults) {
            return initialLocationFieldValue;
        }
        return new Path(getProjectLocationFieldValue());
    }

    private String getProjectLocationFieldValue() {
        return locationPathField == null
                ? "" : locationPathField.getText().trim();
    }

    public IProject getProjectHandle() {
        return getProjectHandle(getProjectName());
    }

    // TODO: move this somewhere appropriate!
    private static IProject getProjectHandle(final String projectName) {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    }

    private String getProjectName() {
        if (projectNameField == null) {
            return initialProjectFieldValue;
        }
        return getProjectNameFieldValue();
    }

    private String getProjectNameFieldValue() {
        return projectNameField == null ? "" : projectNameField.getText()
                .trim();
    }

    private final class UseDefaultsListener extends SelectionAdapter {
        private final Button useDefaultsButton;

        private UseDefaultsListener(final Button useDefaultsButton) {
            this.useDefaultsButton = useDefaultsButton;
        }

        @Override
        public void widgetSelected(
                @SuppressWarnings("unused") final SelectionEvent e) {
            useDefaults = useDefaultsButton.getSelection();
            browseButton.setEnabled(!useDefaults);
            locationPathField.setEnabled(!useDefaults);
            locationLabel.setEnabled(!useDefaults);
            if (useDefaults) {
                customLocationFieldValue = locationPathField.getText();
                setLocationForSelection();
            } else {
                locationPathField.setText(customLocationFieldValue);
            }
        }
    }
}
