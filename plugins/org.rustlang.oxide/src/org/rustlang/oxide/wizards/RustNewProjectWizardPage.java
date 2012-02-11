package org.rustlang.oxide.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class RustNewProjectWizardPage extends WizardNewProjectCreationPage {
    private final IStructuredSelection currentSelection;

    public RustNewProjectWizardPage(
            final IStructuredSelection currentSelection) {
        super("rustNewProjectPage");
        setTitle("Rust Project");
        setDescription("Create a new Rust Project.");
        this.currentSelection = currentSelection;
    }

    @Override
    public void createControl(final Composite parent) {
        super.createControl(parent);
        createWorkingSetGroup((Composite) getControl(), currentSelection,
                new String[] {});
        // TODO: customize.
        // TODO: need to validate project name.
    }
}
