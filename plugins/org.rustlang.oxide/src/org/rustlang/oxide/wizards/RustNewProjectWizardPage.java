package org.rustlang.oxide.wizards;

import java.util.UUID;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.rustlang.oxide.language.model.CrateAttributes;

public class RustNewProjectWizardPage extends WizardNewProjectCreationPage {
    private final IStructuredSelection currentSelection;
    private RustProjectPropertiesGroup properties;

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
        properties = new RustProjectPropertiesGroup(composite,
                parent.getFont());
        createWorkingSetGroup(composite, currentSelection, new String[] {});
    }

    public CrateAttributes createModel() {
        return new CrateAttributes(getProjectName(), properties.getCrateType(),
                properties.getVersion(), UUID.randomUUID(), properties.getUrl(),
                properties.getAuthor(), properties.getLicenseName(),
                properties.getBriefDescription(),
                properties.getLongDescription());
    }
}
