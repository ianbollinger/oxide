package org.rustlang.oxide.launch;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class RustLaunchConfigurationTabGroup extends
        AbstractLaunchConfigurationTabGroup {
    @Override
    public void createTabs(
            @SuppressWarnings("unused") final ILaunchConfigurationDialog dialog,
            @SuppressWarnings("unused") final String mode) {
        final ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
            new RustLaunchConfigurationTab(),
            new EnvironmentTab(),
            new CommonTab()
        };
        setTabs(tabs);
    }
}
