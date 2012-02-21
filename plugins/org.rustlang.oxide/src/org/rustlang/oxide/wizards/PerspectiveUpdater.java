package org.rustlang.oxide.wizards;

import com.google.inject.Inject;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

public class PerspectiveUpdater {
    @Inject
    PerspectiveUpdater() {
    }

    public void update(final IConfigurationElement configuration) {
        BasicNewProjectResourceWizard.updatePerspective(configuration);
    }
}
