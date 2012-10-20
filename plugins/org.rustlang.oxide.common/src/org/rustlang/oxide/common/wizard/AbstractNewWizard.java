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

package org.rustlang.oxide.common.wizard;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nullable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * TODO: Document class.
 */
public abstract class AbstractNewWizard extends Wizard implements INewWizard,
        IExecutableExtension {
    // FIXME: volatility is not sufficient to ensure thread-safety here.
    private volatile IConfigurationElement configuration;
    private volatile IWorkbench workbench;

    @Override
    public void init(final IWorkbench initWorkbench,
            @Nullable final IStructuredSelection selection) {
        this.workbench = initWorkbench;
        setNeedsProgressMonitor(true);
    }

    @Override
    public void setInitializationData(final IConfigurationElement config,
            @Nullable final String propertyName, @Nullable final Object data) {
        // TODO: ensure thread safety.
        this.configuration = config;
    }

    /**
     * TODO: Document method.
     *
     * @return the configuration
     * @throws NullPointerException
     *             if {@link init} has not been called
     */
    public IConfigurationElement getConfiguration() {
        return checkNotNull(configuration);
    }

    /**
     * TODO: Document method.
     *
     * @return the workbench
     * @throws NullPointerException
     *             if {@link init} has not been called
     */
    public IWorkbench getWorkbench() {
        return checkNotNull(workbench);
    }
}
