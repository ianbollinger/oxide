package org.rustlang.oxide.wizards;

import com.google.inject.Inject;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.sapphire.modeling.IExecutableModelElement;
import org.eclipse.sapphire.ui.swt.SapphireWizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class AbstractNewWizard<M extends IExecutableModelElement>
        extends SapphireWizard<M> implements INewWizard, IExecutableExtension {
    private IConfigurationElement configuration;
    private IWorkbench workbench;

    @Inject
    AbstractNewWizard(final M element, final String definition) {
        super(element, definition);
    }

    @Override
    public void init(final IWorkbench workbench,
            @SuppressWarnings("unused") final IStructuredSelection selection) {
        this.workbench = workbench;
        setNeedsProgressMonitor(true);
    }

    @Override
    public void setInitializationData(final IConfigurationElement config,
            @SuppressWarnings("unused") final String propertyName,
            @SuppressWarnings("unused") final Object data) {
        this.configuration = config;
    }

    protected IConfigurationElement getConfiguration() {
        return configuration;
    }

    protected IWorkbench getWorkbench() {
        return workbench;
    }
}
