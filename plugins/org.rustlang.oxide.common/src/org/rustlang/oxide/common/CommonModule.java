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
import javax.annotation.concurrent.Immutable;
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
import org.rustlang.oxide.common.SubProgressMonitorFactory.WorkScale;
import org.rustlang.oxide.common.command.UndoableOperationWithProgress;
import org.rustlang.oxide.common.command.UndoableOperationWithProgressFactory;
import org.slf4j.Logger;

/**
 * TODO: Document class.
 *
 * TODO: list bindings.
 */
@Immutable
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
