package org.rustlang.oxide.launch;

import java.util.List;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleManager;

public class RustLaunchModule extends AbstractModule {
    @Override
    protected void configure() {
    }

    @Provides
    ConsolePlugin provideConsolePlugin() {
        return ConsolePlugin.getDefault();
    }

    @Provides
    IConsoleManager provideConsoleManager(final ConsolePlugin plugin) {
        return plugin.getConsoleManager();
    }

    @Provides
    List<ILaunchConfigurationTab> provideConfigurationTabs() {
        return ImmutableList.of(
            (ILaunchConfigurationTab) new RustLaunchConfigurationTab(),
            (ILaunchConfigurationTab) new EnvironmentTab(),
            (ILaunchConfigurationTab) new CommonTab()
        );
    }
}
