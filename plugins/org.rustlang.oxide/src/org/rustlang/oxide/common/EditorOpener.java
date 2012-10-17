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

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

public class EditorOpener {
    @Inject
    EditorOpener() {
    }

    public void open(final IFile file,
            final IWorkbench workbench) throws PartInitException {
        reveal(file, workbench.getActiveWorkbenchWindow());
        final IWorkbenchWindow window2 = workbench.getActiveWorkbenchWindow();
        if (window2 != null) {
            attemptToOpenActivePage(file, window2);
        }
    }

    private void attemptToOpenActivePage(final IFile file,
            final IWorkbenchWindow window2) throws PartInitException {
        final IWorkbenchPage page = window2.getActivePage();
        if (page != null) {
            final boolean activate = true;
            open(page, file, activate);
        }
    }

    @VisibleForTesting
    void open(final IWorkbenchPage page,
            final IFile file, final boolean activate) throws PartInitException {
        IDE.openEditor(page, file, activate);
    }

    @VisibleForTesting
    void reveal(final IFile file, final IWorkbenchWindow window) {
        BasicNewResourceWizard.selectAndReveal(file, window);
    }
}
