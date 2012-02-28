package org.rustlang.oxide.launch;

import java.util.List;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class RustLaunchConfigurationTabGroup extends
        AbstractLaunchConfigurationTabGroup {
    private final List<ILaunchConfigurationTab> tabs;

    @Inject
    RustLaunchConfigurationTabGroup(final List<ILaunchConfigurationTab> tabs) {
        this.tabs = tabs;
    }

    @Override
    public void createTabs(
            @SuppressWarnings("unused") final ILaunchConfigurationDialog dialog,
            @SuppressWarnings("unused") final String mode) {
        setTabs(Iterables.toArray(tabs, ILaunchConfigurationTab.class));
    }
}
