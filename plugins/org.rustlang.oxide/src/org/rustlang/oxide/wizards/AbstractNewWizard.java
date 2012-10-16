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

package org.rustlang.oxide.wizards;

import javax.annotation.Nullable;
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
    public void init(@SuppressWarnings("null") final IWorkbench initWorkbench,
            @SuppressWarnings("unused") @Nullable
            final IStructuredSelection selection) {
        this.workbench = initWorkbench;
        setNeedsProgressMonitor(true);
    }

    @Override
    public void setInitializationData(
            @SuppressWarnings("null") final IConfigurationElement config,
            @SuppressWarnings("unused") @Nullable final String propertyName,
            @SuppressWarnings("unused") @Nullable final Object data) {
        this.configuration = config;
    }

    @SuppressWarnings("null")
    protected IConfigurationElement getConfiguration() {
        return configuration;
    }

    @SuppressWarnings("null")
    protected IWorkbench getWorkbench() {
        return workbench;
    }
}
