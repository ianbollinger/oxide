package org.rustlang.oxide.common;

import java.nio.charset.Charset;
import com.google.common.base.Charsets;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.statushandlers.StatusManager;
import org.rustlang.oxide.command.UndoableOperationWithProgress;
import org.rustlang.oxide.command.UndoableOperationWithProgressFactory;
import org.rustlang.oxide.common.SubProgressMonitorFactory.WorkScale;
import org.slf4j.Logger;

public class CommonModule extends AbstractModule {
    @Override
    protected void configure() {
        bindConstant().annotatedWith(WorkScale.class).to(1000);
        bind(Charset.class).toInstance(Charsets.UTF_8);
        bind(Logger.class).to(EclipseLogger.class);
        install(new FactoryModuleBuilder()
                .implement(UndoableOperationWithProgress.class,
                        UndoableOperationWithProgress.class)
                .build(UndoableOperationWithProgressFactory.class));
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
    ILog provideLog(final Plugin plugin) {
        return plugin.getLog();
    }

    @Provides
    IPreferenceStore providePreferenceStore(final AbstractUIPlugin plugin) {
        return plugin.getPreferenceStore();
    }

    @Provides
    StatusManager provideStatusManager() {
        return StatusManager.getManager();
    }
}
