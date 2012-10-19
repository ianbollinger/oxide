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

package org.rustlang.oxide.launch;

import com.google.common.base.Optional;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
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
import org.rustlang.oxide.common.Loggers;
import org.slf4j.Logger;

/**
 * Launch shortcut for Rust applications.
 */
public class RustLaunchShortcut implements ILaunchShortcut {
    private final Logger logger;
    private final ILaunchManager launchManager;

    /**
     * This constructor is an implementation detail; do not invoke.
     */
    public RustLaunchShortcut() {
        // TODO: inject fields and make constructor package-private.
        this.logger = OxidePlugin.getLogger();
        this.launchManager = DebugPlugin.getDefault().getLaunchManager();
    }

    @Override
    public void launch(final ISelection selection, final String mode) {
        final Optional<IFile> file = getSelectedFile(selection);
        if (file.isPresent()) {
            try {
                launch(getProjectName(file.get()), mode);
            } catch (final CoreException e) {
                Loggers.logThrowable(logger, e);
            }
        }
    }

    @Override
    public void launch(final IEditorPart editor, final String mode) {
        final IEditorInput editorInput = editor.getEditorInput();
        if (editorInput instanceof FileEditorInput) {
            try {
                launch(getProjectName(getFile(editorInput)), mode);
            } catch (final CoreException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private Optional<IFile> getSelectedFile(final ISelection selection) {
        if (selection instanceof TreeSelection) {
            final Object element =
                    ((TreeSelection) selection).getFirstElement();
            if (element instanceof IFile) {
                return Optional.of((IFile) element);
            }
        }
        return Optional.absent();
    }

    private String getProjectName(final IFile file) {
        return file.getProject().getName();
    }

    private IFile getFile(final IEditorInput editorInput) {
        return ((FileEditorInput) editorInput).getFile();
    }

    private void launch(final String projectName,
            final String mode) throws CoreException {
        // TODO: use new progress monitor.
        final IProgressMonitor monitor = null;
        final boolean build = true;
        final boolean register = true;
        getLaunchConfiguration(projectName).launch(mode, monitor, build,
                register);
    }

    private ILaunchConfiguration getLaunchConfiguration(
            final String projectName) throws CoreException {
        final ILaunchConfigurationType type =
                launchManager.getLaunchConfigurationType(
                        RustApplicationLauncher.ID);
        final IPath projectPath = Path.fromOSString(projectName);
        return findLaunchConfiguration(projectName, type, projectPath)
                .or(createConfiguration(projectName, type, projectPath));
    }

    private ILaunchConfiguration[] getLaunchConfigurations(
            final ILaunchConfigurationType type) throws CoreException {
        return launchManager.getLaunchConfigurations(type);
    }

    private ILaunchConfiguration createConfiguration(final String projectName,
            final ILaunchConfigurationType type,
            final IPath projectPath) throws CoreException {
        final ILaunchConfigurationWorkingCopy workingCopy =
                type.newInstance(null, getConfigurationName(projectPath));
        setConfigurationAttributes(projectName, workingCopy);
        return workingCopy.doSave();
    }

    private void setConfigurationAttributes(final String projectName,
            final ILaunchConfigurationWorkingCopy workingCopy) {
        workingCopy.setAttribute(RustLaunchAttribute.PROJECT.toString(),
                projectName);
        workingCopy.setAttribute(RustLaunchAttribute.EXECUTABLE.toString(),
                projectName);
        workingCopy.setAttribute(RustLaunchAttribute.ARGUMENTS.toString(),
                "");
        workingCopy.setAttribute(DebugPlugin.ATTR_CONSOLE_ENCODING,
                "UTF-8");
    }

    private String getConfigurationName(final IPath projectPath) {
        return launchManager.generateLaunchConfigurationName(
                projectPath.lastSegment());
    }

    private Optional<ILaunchConfiguration> findLaunchConfiguration(
            final String projectName, final ILaunchConfigurationType type,
            final IPath projectPath) throws CoreException {
        for (final ILaunchConfiguration configuration
                : getLaunchConfigurations(type)) {
            if (configurationMatches(projectName, projectPath, configuration)) {
                return Optional.of(configuration);
            }
        }
        return Optional.absent();
    }

    private boolean configurationMatches(final String projectName,
            final IPath projectPath,
            final ILaunchConfiguration configuration) throws CoreException {
        // TODO: implement equality method.
        final String arguments = getAttribute(configuration,
                RustLaunchAttribute.ARGUMENTS);
        final String project = getAttribute(configuration,
                RustLaunchAttribute.PROJECT);
        final String executable = getAttribute(configuration,
                RustLaunchAttribute.EXECUTABLE);
        return arguments.isEmpty()
                && project.equalsIgnoreCase(projectName)
                && projectPath.equals(Path.fromOSString(executable));
    }

    private String getAttribute(final ILaunchConfiguration configuration,
            final RustLaunchAttribute attribute) throws CoreException {
        return configuration.getAttribute(attribute.toString(), "");
    }
}
