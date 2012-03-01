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
        final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        if (window != null) {
            final IWorkbenchPage page = window.getActivePage();
            if (page != null) {
                final boolean activate = true;
                open(page, file, activate);
            }
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
