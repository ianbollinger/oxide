package org.rustlang.oxide.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectReferencePage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.rustlang.oxide.OxideLogger;
import org.rustlang.oxide.OxidePlugin;
import org.rustlang.oxide.RustCreateProjectOperationFactory;
import org.rustlang.oxide.language.model.CrateAttributes;
import org.rustlang.oxide.templates.BasicTemplateContext;

public class RustProjectWizard extends Wizard implements INewWizard,
        IExecutableExtension {
    public static final String ID =
            "org.rustlang.oxide.wizards.RustProjectWizard";
    private final IWorkspace workspace;
    private IConfigurationElement configuration;
    private RustNewProjectWizardPage projectPage;
    private final WizardNewProjectReferencePage referencePage;
    private final WizardPageFactory projectPageFactory;
    private final RustCreateProjectOperationFactory createProjectOperationFactory;
    private final ImageDescriptor imageDescriptor;
    private final OxideLogger logger;

    @Inject
    RustProjectWizard(final WizardNewProjectReferencePage referencePage,
            final WizardPageFactory projectPageFactory,
            final RustCreateProjectOperationFactory createProjectOperationFactory,
            final IWorkspace workspace,
            final ImageDescriptor imageDescriptor,
            final OxideLogger logger) {
        this.referencePage = referencePage;
        this.projectPageFactory = projectPageFactory;
        this.createProjectOperationFactory = createProjectOperationFactory;
        this.workspace = workspace;
        this.imageDescriptor = imageDescriptor;
        this.logger = logger;
    }

    @Override
    public void setInitializationData(final IConfigurationElement config,
            @SuppressWarnings("unused") final String propertyName,
            @SuppressWarnings("unused") final Object data) {
        this.configuration = config;
    }

    @Override
    public void init(@SuppressWarnings("unused") final IWorkbench workbench,
            final IStructuredSelection selection) {
        this.projectPage = projectPageFactory.create(selection);
        setDefaultPageImageDescriptor(imageDescriptor);
    }

    @Override
    public void addPages() {
        addPage(projectPage);
        if (projectsInWorkspace()) {
            addPage(referencePage);
        }
    }

    private boolean projectsInWorkspace() {
        // TODO: LoD violation.
        return workspace.getRoot().getProjects().length > 0;
    }

    // TODO: most of this should be moved elsewhere.
    @Override
    public boolean performFinish() {
        final TemplateContext templateContext = provideTemplateContext(
                projectPage.provideModel());
        final IRunnableWithProgress operation = createProjectOperationFactory.create(
                projectPage.getProjectHandle(), projectPage.getLocationURI(),
                getReferencedProjects(), templateContext);
        final boolean fork = true;
        final boolean cancelable = true;
        try {
            getContainer().run(fork, cancelable, operation);
        } catch (final InterruptedException e) {
            return false;
        } catch (final InvocationTargetException e) {
            logger.log(e);
            return false;
        }
        updatePerspective(configuration);
        return true;
    }

    private List<IProject> getReferencedProjects() {
        if (referencePage == null) {
            return ImmutableList.of();
        }
        return ImmutableList.copyOf(referencePage.getReferencedProjects());
    }

    // TODO: eliminate static call.
    void updatePerspective(final IConfigurationElement config2) {
        BasicNewProjectResourceWizard.updatePerspective(config2);
    }

    // TODO: make this a factory.
    TemplateContext provideTemplateContext(final CrateAttributes model) {
        final TemplateContext templateContext = OxidePlugin.getInstance(
                BasicTemplateContext.class);
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
}
