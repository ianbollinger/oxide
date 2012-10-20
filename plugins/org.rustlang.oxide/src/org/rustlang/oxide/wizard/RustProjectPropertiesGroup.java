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

package org.rustlang.oxide.wizard;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.rustlang.oxide.common.swt.EnumRadioGroup;
import org.rustlang.oxide.common.swt.EnumRadioGroupFactory;
import org.rustlang.oxide.model.CrateType;

/**
 * TODO: Document class.
 */
public class RustProjectPropertiesGroup {
    private final Composite parent;
    private final EnumRadioGroupFactory enumRadioGroupFactory;
    private final GridLayoutFactory gridLayoutFactory;
    private final GridDataFactory gridDataFactory;
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

    @Inject
    RustProjectPropertiesGroup(
            final EnumRadioGroupFactory enumRadioGroupFactory,
            final GridLayoutFactory gridLayoutFactory,
            final GridDataFactory gridDataFactory,
            @Assisted final Composite parent) {
        this.parent = parent;
        this.enumRadioGroupFactory = enumRadioGroupFactory;
        this.gridLayoutFactory = gridLayoutFactory;
        this.gridDataFactory = gridDataFactory;
        this.font = parent.getFont();
        // TODO: replace with factories (ugh.)
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

    CrateType getCrateType() {
        return crateTypeGroup.getSelection();
    }

    String getVersion() {
        return versionField.getText();
    }

    String getUrl() {
        return urlField.getText();
    }

    String getAuthor() {
        return authorField.getText();
    }

    String getLicenseName() {
        return licenseCombo.getText();
    }

    String getBriefDescription() {
        return briefDescriptionField.getText();
    }

    String getLongDescription() {
        return longDescriptionField.getText();
    }

    private void generateLayouts() {
        final GridLayoutFactory twoColumnLayoutFactory =
                gridLayoutFactory.numColumns(2);
        twoColumnLayoutFactory.generateLayout(propertiesGroup);
        twoColumnLayoutFactory.generateLayout(metadataGroup);
        twoColumnLayoutFactory.generateLayout(documentationGroup);
    }

    private Group createGroup(final String title) {
        final Group result = new Group(parent, SWT.NONE);
        result.setFont(font);
        result.setText(title);
        gridDataFactory.align(SWT.FILL, SWT.CENTER).applyTo(result);
        return result;
    }

    private Combo createLicenseCombo() {
        createLabel(metadataGroup, "License");
        final Combo combo = new Combo(metadataGroup, SWT.NONE);
        // TODO: is this the right list of licenses?
        // TODO: Store somewhere appropriate.
        final String[] licenses = {
            "Apache-2.0",
            "BSD-2-clause",
            "MIT",
            "GPL-3.0",
            "LGPL-3.0"
        };
        for (final String license : licenses) {
            combo.add(license);
        }
        combo.setFont(font);
        return combo;
    }

    private EnumRadioGroup<CrateType> createCrateTypeGroup() {
        createLabel(propertiesGroup, "Crate type");
        return enumRadioGroupFactory.create(propertiesGroup, CrateType.class);
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
        text.setFont(font);
        return text;
    }

    private void createLabel(final Composite group, final String text) {
        final Label label = new Label(group, SWT.NONE);
        label.setText(text + ':');
        label.setFont(font);
    }
}
