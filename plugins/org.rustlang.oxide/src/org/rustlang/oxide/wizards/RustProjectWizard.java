package org.rustlang.oxide.wizards;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectReferencePage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.rustlang.oxide.OxidePlugin;
import org.rustlang.oxide.RustCreateProjectOperation;
import org.rustlang.oxide.language.model.CrateAttributes;
import org.rustlang.oxide.templates.BasicTemplateContext;
import org.rustlang.oxide.templates.RustTemplateContextType;

public class RustProjectWizard extends Wizard implements INewWizard,
        IExecutableExtension {
    public static final String ID =
            "org.rustlang.oxide.wizards.RustProjectWizard";
    private IWorkspace workspace;
    private IConfigurationElement config;
    private RustNewProjectWizardPage projectPage;
    private WizardNewProjectReferencePage referencePage;
    private TemplateStore templateStore;

    @Override
    public void setInitializationData(final IConfigurationElement config,
            @SuppressWarnings("unused") final String propertyName,
            @SuppressWarnings("unused") final Object data) {
        this.config = config;
    }

    @Override
    public void init(@SuppressWarnings("unused") final IWorkbench workbench,
            final IStructuredSelection selection) {
        setDefaultPageImageDescriptor(provideImageDescriptor());
        this.workspace = provideWorkspace();
        this.projectPage = provideNewProjectPage(selection);
        this.templateStore = provideTemplateStore();
    }

    @Override
    public void addPages() {
        addPage(projectPage);
        if (projectsInWorkspace()) {
            addPage(provideReferencedProjectsPage());
        }
    }

    private boolean projectsInWorkspace() {
        return workspace.getRoot().getProjects().length > 0;
    }

    // TODO: most of this should be moved elsewhere.
    @Override
    public boolean performFinish() {
        final TemplateContext templateContext = provideTemplateContext(
                projectPage.createModel());
        final IRunnableWithProgress operation = provideCreateProjectOperation(
                projectPage.getProjectHandle(), projectPage.getLocationURI(),
                getReferencedProjects(), workspace, templateStore,
                templateContext);
        final boolean fork = true;
        final boolean cancelable = true;
        try {
            getContainer().run(fork, cancelable, operation);
        } catch (final InterruptedException e) {
            return false;
        } catch (final InvocationTargetException e) {
            log(e);
            return false;
        }
        updatePerspective(config);
        return true;
    }

    private IProject[] getReferencedProjects() {
        return (referencePage != null) ? referencePage.getReferencedProjects()
                : new IProject[] {};
    }

    IWizardPage provideReferencedProjectsPage() {
        final IWizardPage page = new WizardNewProjectReferencePage(
                "rustReferenceProjectPage");
        page.setTitle("Rust Project");
        page.setDescription("Select referenced projects.");
        return page;
    }
    
    RustNewProjectWizardPage provideNewProjectPage(
            final IStructuredSelection selection) {
        return new RustNewProjectWizardPage(selection);
    }

    ImageDescriptor provideImageDescriptor() {
        return OxidePlugin.getImageDescriptor("icons/rust-logo-64x64.png");
    }

    IWorkspace provideWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }

    TemplateStore provideTemplateStore() {
        return OxidePlugin.getTemplateStore();
    }

    void log(final Throwable e) {
        OxidePlugin.getLogger().log(e);
    }

    void updatePerspective(final IConfigurationElement config2) {
        BasicNewProjectResourceWizard.updatePerspective(config2);
    }

    // TODO: move this logic somewhere so it can be tested.
    TemplateContext provideTemplateContext(final CrateAttributes model) {
        final TemplateContext templateContext = new BasicTemplateContext(
                provideTemplateContextType());
        templateContext.setVariable("project_name", model.getName());
        templateContext.setVariable("crate_type", model.getType().getValue());
        templateContext.setVariable("version", model.getVersion());
        templateContext.setVariable("uuid", model.getUuid().toString());
        templateContext.setVariable("url", model.getUrl());
        templateContext.setVariable("author", model.getAuthor());
        templateContext.setVariable("license", model.getLicenseName());
        templateContext.setVariable("brief", model.getBriefDescription());
        templateContext.setVariable("desc", model.getLongDescription());
        return templateContext;
    }

    RustTemplateContextType provideTemplateContextType() {
        return new RustTemplateContextType();
    }

    RustCreateProjectOperation provideCreateProjectOperation(
            final IProject project, final URI location,
            final IProject[] referencedProjects, final IWorkspace workspace2,
            final TemplateStore templateStore2,
            final TemplateContext templateContext) {
        return new RustCreateProjectOperation(project, location,
                referencedProjects, workspace2, templateStore2,
                templateContext, OxidePlugin.getLogger());
    }
}
