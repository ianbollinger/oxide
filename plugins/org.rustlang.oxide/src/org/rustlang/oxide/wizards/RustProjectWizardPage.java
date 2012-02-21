package org.rustlang.oxide.wizards;

import java.util.UUID;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.rustlang.oxide.language.model.CrateAttributes;

public class RustProjectWizardPage extends WizardNewProjectCreationPage {
    private final IStructuredSelection currentSelection;
    private RustProjectPropertiesGroup properties;

    @Inject
    RustProjectWizardPage(
            @Assisted final IStructuredSelection currentSelection) {
        super("rustNewProjectPage");
        setTitle("Rust Project");
        setDescription("Create a new Rust Project.");
        this.currentSelection = currentSelection;
    }

    @Override
    public void createControl(final Composite parent) {
        super.createControl(parent);
        final Composite composite = (Composite) getControl();
        properties = providePropertiesGroup(parent, composite);
        createWorkingSetGroup(composite, currentSelection, new String[] {});
    }

    // TODO: make this a factory.
    private RustProjectPropertiesGroup providePropertiesGroup(
            final Composite parent, final Composite composite) {
        return new RustProjectPropertiesGroup(composite,
                parent.getFont());
    }

    // TODO: make this a factory.
    CrateAttributes provideModel() {
        return new CrateAttributes(getProjectName(), properties.getCrateType(),
                properties.getVersion(), UUID.randomUUID(), properties.getUrl(),
                properties.getAuthor(), properties.getLicenseName(),
                properties.getBriefDescription(),
                properties.getLongDescription());
    }
}
