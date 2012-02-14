package org.rustlang.oxide.launch;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.rustlang.oxide.OxidePlugin;

public class RustLaunchShortcut implements ILaunchShortcut {
    @Override
    public void launch(final ISelection selection, final String mode) {
        if (selection == null || !(selection instanceof TreeSelection)) {
            return;
        }
        final TreeSelection treeSelection = (TreeSelection) selection;
        final Object element = treeSelection.getFirstElement();
        if (!(element instanceof IFile)) {
            return;
        }
        final IFile file = (IFile) element;
        final String projectName = file.getProject().getName();
        try {
            launch(projectName, mode);
        } catch (final CoreException e) {
            OxidePlugin.log(e);
        }
    }

    @Override
    public void launch(final IEditorPart editor, final String mode) {
        final IEditorInput editorInput = editor.getEditorInput();
        if (editorInput == null || !(editorInput instanceof FileEditorInput)) {
            return;
        }
        final FileEditorInput fileEditorInput = (FileEditorInput) editorInput;
        final IFile file = fileEditorInput.getFile();
        final String projectName = file.getProject().getName();
        try {
            launch(projectName, mode);
        } catch (final CoreException e) {
            OxidePlugin.log(e);
        }
    }

    private void launch(final String projectName, final String mode)
            throws CoreException {
        ILaunchConfiguration found = null;
        final ILaunchManager launchManager = DebugPlugin.getDefault()
                .getLaunchManager();
        final ILaunchConfigurationType type = launchManager
                .getLaunchConfigurationType(RustApplicationLauncher.ID);
        final ILaunchConfiguration[] configurations = launchManager
                .getLaunchConfigurations(type);
        // TODO: implement equality method.
        for (final ILaunchConfiguration configuration : configurations) {
            final String project = configuration.getAttribute(
                    RustLaunchAttribute.PROJECT.toString(), "");
            final String executable = configuration.getAttribute(
                    RustLaunchAttribute.EXECUTABLE.toString(), "");
            final String arguments = configuration.getAttribute(
                    RustLaunchAttribute.ARGUMENTS.toString(), "");
            if (arguments.isEmpty()
                    && project.equalsIgnoreCase(projectName)
                    && Path.fromOSString(projectName).equals(
                            Path.fromOSString(executable))) {
                found = configuration;
                break;
            }
        }
        if (found == null) {
            final String configurationName = launchManager
                    .generateLaunchConfigurationName(Path.fromOSString(
                            projectName).lastSegment());
            final ILaunchConfigurationWorkingCopy workingCopy = type
                    .newInstance(null, configurationName);
            workingCopy.setAttribute(RustLaunchAttribute.PROJECT.toString(),
                    projectName);
            workingCopy.setAttribute(RustLaunchAttribute.EXECUTABLE.toString(),
                    projectName);
            workingCopy.setAttribute(RustLaunchAttribute.ARGUMENTS.toString(),
                    "");
            workingCopy
                    .setAttribute(DebugPlugin.ATTR_CONSOLE_ENCODING, "UTF-8");
            found = workingCopy.doSave();
        }
        final IProgressMonitor monitor = null;
        final boolean build = true;
        final boolean register = true;
        found.launch(mode, monitor, build, register);
    }
}
