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
import org.rustlang.oxide.common.swt.EnumRadioGroup;
import org.rustlang.oxide.language.model.CrateType;

// TODO: is there a point to allow having the crate name be different than the
// project name?
// TODO: implement validation in general.
public class RustProjectPropertiesGroup {
    private final Composite parent;
    private final Font font;
    private final Group propertiesGroup;
    private final Group metadataGroup;
    private final Group documentationGroup;
    private final EnumRadioGroup<CrateType> crateTypeGroup;
    private final Text versionField;
    private final Text urlField;
    private final Text authorField;
    private final Combo licenseCombo;
    private final Text briefDescriptionField;
    private final Text longDescriptionField;

    RustProjectPropertiesGroup(final Composite parent, final Font font) {
        this.parent = parent;
        this.font = font;
        this.propertiesGroup = createGroup("Crate properties");
        this.crateTypeGroup = createCrateTypeGroup();
        this.versionField = createVersionField();
        this.urlField = createTextField(propertiesGroup, "URL");
        this.metadataGroup = createGroup("Crate metadata");
        this.authorField = createTextField(metadataGroup, "Author");
        authorField.setText(System.getProperty("user.name"));
        this.licenseCombo = createLicenseCombo();
        this.documentationGroup = createGroup("Crate documentation");
        this.briefDescriptionField = createTextField(documentationGroup,
                "Brief");
        this.longDescriptionField = createLongDescriptionField();
        generateLayouts();
    }

    private void generateLayouts() {
        final GridLayoutFactory twoColumnLayoutFactory = GridLayoutFactory
                .swtDefaults().numColumns(2);
        twoColumnLayoutFactory.generateLayout(propertiesGroup);
        twoColumnLayoutFactory.generateLayout(metadataGroup);
        twoColumnLayoutFactory.generateLayout(documentationGroup);
    }

    private Group createGroup(final String title) {
        final Group result = new Group(parent, SWT.NONE);
        result.setFont(font);
        result.setText(title);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER)
                .applyTo(result);
        return result;
    }

    private Combo createLicenseCombo() {
        createLabel(metadataGroup, "License");
        final Combo combo = new Combo(metadataGroup, SWT.NONE);
        // TODO: is this the right list of licenses?
        // TODO: Store somewhere appropriate.
        combo.add("Apache-2.0");
        combo.add("BSD-2-clause");
        combo.add("MIT");
        combo.add("GPL-3.0");
        combo.add("LGPL-3.0");
        combo.setFont(font);
        return combo;
    }

    private EnumRadioGroup<CrateType> createCrateTypeGroup() {
        createLabel(propertiesGroup, "Crate type");
        return EnumRadioGroup.of(propertiesGroup, CrateType.class);
    }

    private Text createTextField(final Group group, final String label) {
        createLabel(group, label);
        final Text text = new Text(group, SWT.BORDER);
        text.setFont(font);
        return text;
    }

    private Text createLongDescriptionField() {
        createLabel(documentationGroup, "Description");
        final Text text = new Text(documentationGroup, SWT.BORDER | SWT.MULTI
                | SWT.WRAP);
        text.setFont(font);
        return text;
    }

    private Text createVersionField() {
        final Text text = createTextField(propertiesGroup, "Version");
        text.setText("0.0.1");
        return text;
    }

    private void createLabel(final Composite group, final String text) {
        final Label label = new Label(group, SWT.NONE);
        label.setText(text + ':');
        label.setFont(font);
    }

    public CrateType getCrateType() {
        return crateTypeGroup.getSelection();
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
