package org.rustlang.oxide;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ILog;
import org.eclipse.jface.preference.IPreferenceStore;

public class OxideModule extends AbstractModule {
    @Override
    protected void configure() {
    }

    @Provides
    IPreferenceStore providePreferenceStore(final OxidePlugin plugin) {
        return plugin.getPreferenceStore();
    }

    @Provides @Singleton
    ILog provideLog(final OxidePlugin plugin) {
        return plugin.getLog();
    }

    @Provides
    IWorkspace provideWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }

    @Provides
    IWorkspaceRoot provideWorkspaceRoot(final IWorkspace workspace) {
        return workspace.getRoot();
    }

    @Provides
    OxidePlugin providePlugin() {
        return OxidePlugin.getDefault();
    }
}
