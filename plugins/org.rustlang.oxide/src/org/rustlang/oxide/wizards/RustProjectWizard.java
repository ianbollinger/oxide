package org.rustlang.oxide.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
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
import org.rustlang.oxide.OxideLogger;
import org.rustlang.oxide.OxidePlugin;
import org.rustlang.oxide.RustCreateProjectOperationFactory;
import org.rustlang.oxide.language.model.CrateAttributes;
import org.rustlang.oxide.templates.BasicTemplateContext;

public class RustProjectWizard extends Wizard implements INewWizard,
        IExecutableExtension {
    public static final String ID =
            "org.rustlang.oxide.wizards.RustProjectWizard";
    private IConfigurationElement configuration;
    private RustProjectWizardPage projectPage;
    private final IWorkspaceRoot workspaceRoot;
    private final WizardNewProjectReferencePage referencePage;
    private final RustProjectWizardPageFactory projectPageFactory;
    private final RustCreateProjectOperationFactory newProjectOperationFactory;
    private final ImageDescriptor imageDescriptor;
    private final PerspectiveUpdater perspectiveUpdater;
    private final OxideLogger logger;

    @Inject
    RustProjectWizard(final WizardNewProjectReferencePage referencePage,
            final RustProjectWizardPageFactory projectPageFactory,
            final RustCreateProjectOperationFactory newProjectOperationFactory,
            final IWorkspaceRoot workspaceRoot,
            final ImageDescriptor imageDescriptor,
            final PerspectiveUpdater perspectiveUpdater,
            final OxideLogger logger) {
        this.referencePage = referencePage;
        this.projectPageFactory = projectPageFactory;
        this.newProjectOperationFactory = newProjectOperationFactory;
        this.workspaceRoot = workspaceRoot;
        this.imageDescriptor = imageDescriptor;
        this.perspectiveUpdater = perspectiveUpdater;
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
        return workspaceRoot.getProjects().length > 0;
    }

    // TODO: most of this should be moved elsewhere.
    @Override
    public boolean performFinish() {
        final TemplateContext templateContext = provideTemplateContext(
                projectPage.provideModel());
        final IRunnableWithProgress operation = newProjectOperationFactory.create(
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
        perspectiveUpdater.update(configuration);
        return true;
    }

    private List<IProject> getReferencedProjects() {
        if (referencePage == null) {
            return ImmutableList.of();
        }
        return ImmutableList.copyOf(referencePage.getReferencedProjects());
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
