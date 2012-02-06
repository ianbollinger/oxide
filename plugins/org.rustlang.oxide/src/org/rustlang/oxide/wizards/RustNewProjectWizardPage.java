package org.rustlang.oxide.wizards;

import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class RustNewProjectWizardPage extends WizardNewProjectCreationPage {
    RustNewProjectWizardPage(final String pageName) {
        super(pageName);
        setTitle("Rust Project");
        setDescription("Create a new Rust Project.");
    }
}
