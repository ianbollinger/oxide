package org.rustlang.oxide.wizards;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.*;
import org.eclipse.ui.*;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectReferencePage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.rustlang.oxide.OxidePlugin;
import org.rustlang.oxide.RustProjects;

public class RustProjectWizard extends Wizard implements INewWizard,
        IExecutableExtension {
    public static final String WIZARD_ID =
            "org.rustlang.oxide.wizards.RustProjectWizard";
    private IConfigurationElement config;
    private RustProjectWizardPage projectPage;
    private IProject createdProject;
    private WizardNewProjectReferencePage referencePage;

    private void addProjectReferencePage() {
        if (ResourcesPlugin.getWorkspace().getRoot().getProjects().length > 0) {
            referencePage = new WizardNewProjectReferencePage("Reference Page");
            referencePage.setTitle("Reference page");
            referencePage.setDescription("Select referenced projects");
            this.addPage(referencePage);
        }
    }

    @Override
    public void addPages() {
        addPage(projectPage);
        addProjectReferencePage();
    }

    @Override
    public void init(@SuppressWarnings("unused") final IWorkbench workbench,
            @SuppressWarnings("unused") final IStructuredSelection selection) {
        initializeDefaultPageImageDescriptor();
        projectPage = createProjectPage();
    }

    private void initializeDefaultPageImageDescriptor() {
        final ImageDescriptor desc = OxidePlugin
                .getImageDescriptor("icons/rust-logo-64x64.png");
        setDefaultPageImageDescriptor(desc);
    }

    private RustProjectWizardPage createProjectPage() {
        return new RustProjectWizardPage("Setting project properties");
    }

    @Override
    public void setInitializationData(final IConfigurationElement config,
            @SuppressWarnings("unused") final String propertyName,
            @SuppressWarnings("unused") final Object data) {
        this.config = config;
    }

    @Override
    public boolean performFinish() {
        createdProject = createNewProject();
        BasicNewProjectResourceWizard.updatePerspective(config);
        return true;
    }

    private IProject createNewProject() {
        final IProject newProjectHandle = projectPage.getProjectHandle();
        final IPath defaultPath = Platform.getLocation();
        IPath newPath = projectPage.getLocationPath();
        if (defaultPath.equals(newPath)) {
            newPath = null;
        } else {
            final IPath withName = defaultPath.append(newProjectHandle
                    .getName());
            if (newPath.toFile().equals(withName.toFile())) {
                newPath = null;
            }
        }
        final IWorkspace workspace = ResourcesPlugin.getWorkspace();
        final IProjectDescription description = workspace
                .newProjectDescription(newProjectHandle.getName());
        description.setLocation(newPath);
        if (referencePage != null) {
            final IProject[] refProjects = referencePage
                    .getReferencedProjects();
            if (refProjects.length > 0) {
                description.setReferencedProjects(refProjects);
            }
        }
        final WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
            @Override
            protected void execute(IProgressMonitor monitor) {
                RustProjects.create(description, newProjectHandle, monitor);
            }
        };
        try {
            getContainer().run(true, true, op);
        } catch (final InterruptedException e) {
            return null;
        } catch (final InvocationTargetException e) {
            final Throwable t = e.getTargetException();
            if (t instanceof CoreException) {
                final int code = ((CoreException) t).getStatus().getCode();
                if (code == IResourceStatus.CASE_VARIANT_EXISTS) {
                    MessageDialog.openError(getShell(),
                            "Unable to create project",
                            "Another project with the same name (and different "
                                    + "case) already exists.");
                } else {
                    ErrorDialog.openError(getShell(),
                            "Unable to create project", null,
                            ((CoreException) t).getStatus());
                }
            } else {
                OxidePlugin.log(IStatus.ERROR, t.toString(), t);
                MessageDialog.openError(getShell(), "Unable to create project",
                        t.getMessage());
            }
            return null;
        }
        return newProjectHandle;
    }

    public IProject getCreatedProject() {
        return createdProject;
    }
}
