package org.rustlang.oxide.wizards;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class RustNewProjectWizardPage extends WizardNewProjectCreationPage {
    public RustNewProjectWizardPage() {
        super("rustNewProjectPage");
        setTitle("Rust Project");
        setDescription("Create a new Rust Project.");
    }

    @Override
    public void createControl(final Composite parent) {
        super.createControl(parent);
        // TODO: customize.
        // TODO: need to validate project name.
    }
}
