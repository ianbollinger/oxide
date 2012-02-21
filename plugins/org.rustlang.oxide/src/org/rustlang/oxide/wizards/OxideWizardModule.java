package org.rustlang.oxide.wizards;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.dialogs.WizardNewProjectReferencePage;
import org.rustlang.oxide.OxidePlugin;
import org.rustlang.oxide.RustCreateProjectOperation;
import org.rustlang.oxide.RustCreateProjectOperationFactory;

public class OxideWizardModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(RustNewProjectWizardPage.class,
                        RustNewProjectWizardPage.class)
                .build(WizardPageFactory.class));
        install(new FactoryModuleBuilder()
                .implement(RustCreateProjectOperation.class,
                        RustCreateProjectOperation.class)
                .build(RustCreateProjectOperationFactory.class));
    }

    @Provides
    WizardNewProjectReferencePage provideReferencedProjectsPage() {
        final WizardNewProjectReferencePage page =
                new WizardNewProjectReferencePage("rustReferenceProjectPage");
        page.setTitle("Rust Project");
        page.setDescription("Select referenced projects.");
        return page;
    }

    @Provides
    ImageDescriptor provideImageDescriptor() {
        return OxidePlugin.getImageDescriptor("icons/rust-logo-64x64.png");
    }
}
