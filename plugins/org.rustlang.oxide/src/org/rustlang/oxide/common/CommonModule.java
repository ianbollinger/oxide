/*
 * Copyright 2012 Ian D. Bollinger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
        final IWorkspace workspace = ResourcesPlugin.getWorkspace();
        assert workspace != null;
        return workspace;
    }

    @Provides
    IWorkspaceRoot provideWorkspaceRoot(final IWorkspace workspace) {
        final IWorkspaceRoot root = workspace.getRoot();
        assert root != null;
        return root;
    }

    @Provides
    ILog provideLog(final Plugin plugin) {
        final ILog log = plugin.getLog();
        assert log != null;
        return log;
    }

    @Provides
    IPreferenceStore providePreferenceStore(final AbstractUIPlugin plugin) {
        final IPreferenceStore preferenceStore = plugin.getPreferenceStore();
        assert preferenceStore != null;
        return preferenceStore;
    }

    @Provides
    StatusManager provideStatusManager() {
        final StatusManager manager = StatusManager.getManager();
        assert manager != null;
        return manager;
    }
}
