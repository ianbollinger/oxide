package org.rustlang.oxide.wizards;

import java.lang.reflect.InvocationTargetException;
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
import org.rustlang.oxide.templates.BasicTemplateContext;
import org.rustlang.oxide.templates.RustTemplateContextType;

// TODO: Handle working sets.
public class RustProjectWizard extends Wizard implements INewWizard,
        IExecutableExtension {
    public static final String ID =
            "org.rustlang.oxide.wizards.RustProjectWizard";
    private IWorkspace workspace;
    private IConfigurationElement config;
    private RustNewProjectWizardPage projectPage;
    private WizardNewProjectReferencePage referencePage;

    @Override
    public void init(@SuppressWarnings("unused") final IWorkbench workbench,
            @SuppressWarnings("unused") final IStructuredSelection selection) {
        final ImageDescriptor descriptor = OxidePlugin
                .getImageDescriptor("icons/rust-logo-64x64.png");
        setDefaultPageImageDescriptor(descriptor);
        this.workspace = ResourcesPlugin.getWorkspace();
        this.projectPage = new RustNewProjectWizardPage();
    }

    @Override
    public void addPages() {
        addPage(projectPage);
        if (projectsInWorkspace()) {
            addPage(createReferencePage());
        }
    }

    private boolean projectsInWorkspace() {
        return workspace.getRoot().getProjects().length > 0;
    }

    private IWizardPage createReferencePage() {
        final IWizardPage page = new WizardNewProjectReferencePage(
                "rustReferenceProjectPage");
        page.setTitle("Rust Project");
        page.setDescription("Select referenced projects.");
        return page;
    }

    @Override
    public void setInitializationData(final IConfigurationElement config,
            @SuppressWarnings("unused") final String propertyName,
            @SuppressWarnings("unused") final Object data) {
        this.config = config;
    }

    @Override
    public boolean performFinish() {
        final IProject newProjectHandle = projectPage.getProjectHandle();
        final IProject[] referencedProjects = getReferencedProjects();
        final TemplateStore templateStore = OxidePlugin.getTemplateStore();
        final TemplateContext templateContext = new BasicTemplateContext(
                new RustTemplateContextType());
        final IRunnableWithProgress operation = new RustCreateProjectOperation(
                newProjectHandle, referencedProjects, workspace, templateStore,
                templateContext);
        final boolean fork = true;
        final boolean cancelable = true;
        try {
            getContainer().run(fork, cancelable, operation);
        } catch (final InterruptedException e) {
            return false;
        } catch (final InvocationTargetException e) {
            OxidePlugin.log(e);
            return false;
        }
        BasicNewProjectResourceWizard.updatePerspective(config);
        return true;
    }

    private IProject[] getReferencedProjects() {
        return (referencePage != null)
                ? referencePage.getReferencedProjects()
                : new IProject[] {};
    }
}
