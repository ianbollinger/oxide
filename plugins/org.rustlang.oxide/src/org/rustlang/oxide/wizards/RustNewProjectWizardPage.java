package org.rustlang.oxide.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class RustNewProjectWizardPage extends WizardNewProjectCreationPage {
    private final IStructuredSelection currentSelection;
    private RustProjectPropertiesGroup projectPropertiesGroup;

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
        final Composite composite = (Composite) getControl();
        projectPropertiesGroup = new RustProjectPropertiesGroup(composite, parent.getFont());
        createWorkingSetGroup(composite, currentSelection, new String[] {});
    }
}
