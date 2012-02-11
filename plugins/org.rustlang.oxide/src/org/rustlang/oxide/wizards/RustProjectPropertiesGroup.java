package org.rustlang.oxide.wizards;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

// TODO: is there a point to allow having the crate name be different
// than the project name?
// TODO: separate into different groups
// TODO: using jface's databinding API might tidy things up.
// TODO: implement validation in general.
public class RustProjectPropertiesGroup {
    private final Composite parent;
    private final Font font;
    private final Group group;
    private final Composite crateTypeGroup;
    private final Text versionField;
    private final Text urlField;
    private final Text authorField;
    private final Combo licenseCombo;
    private final Text briefDescriptionField;
    private final Text longDescriptionField;

    RustProjectPropertiesGroup(final Composite parent, final Font font) {
        this.parent = parent;
        this.font = font;
        this.group = createGroup();
        this.crateTypeGroup = createCrateTypeGroup();
        this.versionField = createVersionField();
        // TODO: do we want a default URL?
        // e.g. https://github.com/<author>/<project>
        this.urlField = createTextField("URL");
        // TODO: set author to user.name.
        this.authorField = createTextField("Author");
        this.licenseCombo = createLicenseCombo();
        this.briefDescriptionField = createTextField("Brief");
        this.longDescriptionField = createLongDescriptionField();
        GridLayoutFactory.swtDefaults().numColumns(2).generateLayout(group);
    }

    private Group createGroup() {
        final Group result = new Group(parent, SWT.NONE);
        result.setFont(font);
        result.setText("Properties");
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER)
                .applyTo(result);
        return result;
    }

    private Combo createLicenseCombo() {
        createLabel("License");
        final Combo combo = new Combo(group, SWT.NONE);
        // TODO: is this the right list of licenses? Store somewhere
        // appropriate.
        combo.add("Apache-2.0");
        combo.add("BSD-2-clause");
        combo.add("MIT");
        combo.add("GPL-3.0");
        combo.add("LGPL-3.0");
        combo.setFont(font);
        return combo;
    }

    private Composite createCrateTypeGroup() {
        createLabel("Crate type");
        // TODO: too much space between buttons.
        final Composite radioButtonGroup = new Composite(group, SWT.NONE);
        // TODO: select a default.
        createRadioButton("Library", radioButtonGroup);
        createRadioButton("Binary", radioButtonGroup);
        GridLayoutFactory.swtDefaults().numColumns(2)
                .generateLayout(radioButtonGroup);
        return radioButtonGroup;
    }

    private Button createRadioButton(final String text,
            final Composite radioButtonGroup) {
        final Button button = new Button(radioButtonGroup, SWT.RADIO);
        button.setText(text);
        button.setFont(font);
        return button;
    }

    private Text createTextField(final String label) {
        createLabel(label);
        final Text text = new Text(group, SWT.BORDER);
        text.setFont(font);
        return text;
    }

    private Text createLongDescriptionField() {
        createLabel("Long");
        final Text text = new Text(group, SWT.BORDER | SWT.MULTI | SWT.WRAP);
        text.setFont(font);
        return text;
    }

    private Text createVersionField() {
        final Text text = createTextField("Version");
        // TODO: decide on versioning scheme.
        text.setText("0.0.1");
        return text;
    }

    private void createLabel(final String text) {
        final Label label = new Label(group, SWT.NONE);
        label.setText(text + ':');
        label.setFont(font);
    }

    public String getVersion() {
        return versionField.getText();
    }

    public String getUrl() {
        return urlField.getText();
    }

    public String getAuthor() {
        return authorField.getText();
    }

    public String getLicenseName() {
        return licenseCombo.getText();
    }

    public String getBriefDescription() {
        return briefDescriptionField.getText();
    }

    public String getLongDescription() {
        return longDescriptionField.getText();
    }
}
