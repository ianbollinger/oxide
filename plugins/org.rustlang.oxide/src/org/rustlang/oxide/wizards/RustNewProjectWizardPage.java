package org.rustlang.oxide.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;

public class RustNewProjectWizardPage extends WizardNewProjectCreationPage {
    private final IStructuredSelection currentSelection;

    public RustNewProjectWizardPage(final IStructuredSelection currentSelection) {
        super("rustNewProjectPage");
        setTitle("Rust Project");
        setDescription("Create a new Rust Project.");
        this.currentSelection = currentSelection;
    }

    @Override
    public void createControl(final Composite parent) {
        super.createControl(parent);
        final Composite composite = (Composite) getControl();

        // TODO: extract class
        final Font font = parent.getFont();
        final Group propertiesGroup = new Group(composite, SWT.NONE);
        propertiesGroup.setFont(composite.getFont());
        propertiesGroup.setText("Properties");
        final GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        propertiesGroup.setLayout(layout);
        propertiesGroup.setLayoutData(new GridData(GridData.FILL_BOTH));

        // TODO: is there a point to allow having the crate name be different
        // than the project name?

        createLabel("Crate type:", propertiesGroup, font);
        createRadioButtons(propertiesGroup, font);

        // TODO: separate into different groups
        createLabel("Version:", propertiesGroup, font);
        createVersionField(propertiesGroup, font);
        // TODO: do we want a default URL?
        // TODO: e.g. https://github.com/<author>/<project>
        createLabel("URL:", propertiesGroup, font);
        createUrlField(propertiesGroup, font);
        // TODO: set author to user.name.
        createLabel("Author:", propertiesGroup, font);
        createAuthorField(propertiesGroup, font);

        createLabel("License:", propertiesGroup, font);
        final Combo combo = new Combo(propertiesGroup, SWT.NONE);
        combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        // TODO: is this the right list of licenses? Store somewhere
        // appropriate.
        combo.add("Apache-2.0");
        combo.add("BSD-2-clause");
        combo.add("MIT");
        combo.add("GPL-3.0");
        combo.add("LGPL-3.0");
        combo.setFont(font);

        createLabel("Brief:", propertiesGroup, font);
        createBriefField(propertiesGroup, font);

        createLabel("Long:", propertiesGroup, font);
        createLongField(propertiesGroup, font);

        createWorkingSetGroup(composite, currentSelection, new String[] {});
        // TODO: need to validate project name.
    }

    private void createRadioButtons(final Composite composite, final Font font) {
        // TODO: too much space between buttons.
        final Composite radioButtonGroup = new Composite(composite, SWT.NONE);
        final GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        radioButtonGroup.setLayout(layout);
        radioButtonGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        // TODO: select a default.
        createRadioButton("Library", radioButtonGroup, font);
        createRadioButton("Binary", radioButtonGroup, font);
    }

    private void createRadioButton(final String text,
            final Composite radioButtonGroup, final Font font) {
        final Button button = new Button(radioButtonGroup, SWT.RADIO);
        final GridData data = new GridData(GridData.FILL_HORIZONTAL);
        button.setLayoutData(data);
        button.setText(text);
        button.setFont(font);
    }

    private void createBriefField(final Group projectGroup, final Font font) {
        final Text text = new Text(projectGroup, SWT.BORDER);
        final GridData data = new GridData(GridData.FILL_HORIZONTAL);
        text.setLayoutData(data);
        text.setFont(font);
    }

    private void createLongField(final Group projectGroup, final Font font) {
        final Text text = new Text(projectGroup, SWT.BORDER | SWT.MULTI
                | SWT.WRAP);
        final GridData data = new GridData(GridData.FILL_BOTH);
        text.setLayoutData(data);
        text.setFont(font);
    }

    private void createAuthorField(final Group projectGroup, final Font font) {
        final Text text = new Text(projectGroup, SWT.BORDER);
        final GridData data = new GridData(GridData.FILL_HORIZONTAL);
        text.setLayoutData(data);
        text.setFont(font);
    }

    private Text createVersionField(final Group projectGroup, final Font font) {
        final Text text = new Text(projectGroup, SWT.BORDER);
        final GridData data = new GridData(GridData.FILL_HORIZONTAL);
        text.setLayoutData(data);
        // TODO: decide on versioning scheme.
        text.setText("0.0.1");
        text.setFont(font);
        return text;
    }

    private void createUrlField(final Group projectGroup, final Font font) {
        final Text text = new Text(projectGroup, SWT.BORDER);
        final GridData data = new GridData(GridData.FILL_HORIZONTAL);
        text.setLayoutData(data);
        text.setFont(font);
    }

    private void createLabel(final String text, final Group projectGroup,
            final Font font) {
        final Label label = new Label(projectGroup, SWT.NONE);
        label.setText(text);
        label.setFont(font);
    }
}
