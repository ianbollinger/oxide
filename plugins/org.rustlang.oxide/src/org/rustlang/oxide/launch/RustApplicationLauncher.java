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

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.File;
import java.util.List;
import javax.annotation.Nullable;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2;
import org.eclipse.jface.util.Util;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.rustlang.oxide.nature.RustNature;

public class RustApplicationLauncher implements ILaunchConfigurationDelegate2 {
    public static final String ID = "org.rustlang.oxide.launch.configuration";
    private final IWorkspaceRoot workspaceRoot;
    private final IConsoleManager consoleManager;

    public RustApplicationLauncher(final IWorkspaceRoot root,
            final IConsoleManager consoleManager) {
        this.workspaceRoot = root;
        this.consoleManager = consoleManager;
    }

    @Override
    public boolean buildForLaunch(
            @SuppressWarnings("null") final ILaunchConfiguration configuration,
            @SuppressWarnings("unused") @Nullable final String mode,
            @SuppressWarnings("unused") @Nullable
            final IProgressMonitor monitor) throws CoreException {
        final String project = getAttribute(configuration,
                RustLaunchAttribute.PROJECT);
        final String executable = getAttribute(configuration,
                RustLaunchAttribute.EXECUTABLE);
        return !project.isEmpty() && !executable.isEmpty();
    }

    @Override
    public boolean finalLaunchCheck(
            @SuppressWarnings("unused") @Nullable
            final ILaunchConfiguration config,
            @SuppressWarnings("unused") @Nullable final String mode,
            @SuppressWarnings("unused") @Nullable
            final IProgressMonitor monitor) {
        return true;
    }

    @Override
    public ILaunch getLaunch(
            @SuppressWarnings("null") final ILaunchConfiguration configuration,
            @SuppressWarnings("null") final String mode) {
        // TODO: make a factory.
        return new Launch(configuration, mode, null);
    }

    @Override
    public boolean preLaunchCheck(
            @SuppressWarnings("unused") @Nullable
            final ILaunchConfiguration configuration,
            @SuppressWarnings("unused") @Nullable final String mode,
            @SuppressWarnings("unused") @Nullable
            final IProgressMonitor monitor) {
        return true;
    }

    @Override
    public void launch(
            @SuppressWarnings("null") final ILaunchConfiguration configuration,
            @SuppressWarnings("unused") @Nullable final String mode,
            @SuppressWarnings("null") final ILaunch launch,
            @SuppressWarnings("unused") @Nullable
            final IProgressMonitor monitor) throws CoreException {
        execute(configuration, launch);
    }

    private Optional<IProject> getProject(
            final ILaunchConfiguration configuration) throws CoreException {
        final IResource resource = workspaceRoot.findMember(
                getProjectName(configuration));
        return isOpenRustProject(resource)
                ? Optional.of((IProject) resource)
                : Optional.<IProject>absent();
    }

    private String getProjectName(
            final ILaunchConfiguration configuration) throws CoreException {
        return getAttribute(configuration, RustLaunchAttribute.PROJECT);
    }

    private boolean isOpenRustProject(
            final IResource resource) throws CoreException {
        if (resource instanceof IProject) {
            final IProject project = (IProject) resource;
            return project.isOpen() && project.hasNature(RustNature.ID);
        }
        return false;
    }

    private void execute(final ILaunchConfiguration configuration,
            final ILaunch launch) throws CoreException {
        final Optional<IProject> possibleProject = getProject(configuration);
        if (possibleProject.isPresent()) {
            spawnProcess(configuration, launch, possibleProject.get());
        }
    }

    private void spawnProcess(final ILaunchConfiguration configuration,
            final ILaunch launch,
            final IProject project) throws CoreException {
        final String executableName = getExecutableName(configuration);
        final IPath projectPath = project.getLocation();
        final List<String> arguments = getArguments(configuration,
                executableName, projectPath);
        spawnProcess(launch, executableName, getWorkingDirectory(projectPath),
                arguments);
    }

    private File getWorkingDirectory(final IPath projectPath) {
        return new File(projectPath.toOSString());
    }

    private List<String> getArguments(final ILaunchConfiguration configuration,
            final String executableName,
            final IPath projectPath) throws CoreException {
        final String programArguments = getAttribute(configuration,
                RustLaunchAttribute.ARGUMENTS);
        final IPath executablePath = getExecutablePath(executableName);
        final String commandLine = checkNotNull(
                projectPath.append(executablePath).toOSString());
        return ImmutableList.<String>builder()
                .add(commandLine)
                .addAll(argumentsAsList(programArguments))
                .build();
    }

    private String getExecutableName(
            final ILaunchConfiguration configuration) throws CoreException {
        final String executable = getAttribute(configuration,
                RustLaunchAttribute.EXECUTABLE);
        final IPath relativeExecutablePath = Path.fromOSString(executable);
        return relativeExecutablePath.lastSegment();
    }

    private static String getAttribute(final ILaunchConfiguration configuration,
            final RustLaunchAttribute attribute) throws CoreException {
        return configuration.getAttribute(attribute.toString(), "");
    }

    // TODO: create a strategy for this.
    void spawnProcess(final ILaunch launch, final String executableName,
            final File workingDirectory,
            final Iterable<String> arguments) throws CoreException {
        final String[] array = Iterables.toArray(arguments, String.class);
        final Process process = DebugPlugin.exec(array, workingDirectory);
        DebugPlugin.newProcess(launch, process, executableName);
    }

    private IPath getExecutablePath(final String executableName) {
        return Util.isWindows()
                ? Path.fromOSString(executableName)
                : Path.fromOSString(".").append(executableName);
    }

    public MessageConsole findMessageConsole(final String name) {
        for (final IConsole element : consoleManager.getConsoles()) {
            if (name.equals(element.getName())) {
                return (MessageConsole) element;
            }
        }
        return createMessageConsole(name);
    }

    private MessageConsole createMessageConsole(final String name) {
        // TODO: use factory.
        final MessageConsole console = new MessageConsole(name, null);
        consoleManager.addConsoles(new IConsole[] { console });
        return console;
    }

    // TODO: move this somewhere appropriate.
    private List<String> argumentsAsList(@Nullable final String arguments) {
        final List<String> argumentsList = Lists.newArrayList();
        if (arguments == null || arguments.trim().isEmpty()) {
            return argumentsList;
        }
        boolean inQuote = false;
        boolean escape = false;
        final StringBuilder argument = new StringBuilder();
        // TODO: this does not handle characters outside the BMP.
        for (int i = 0; i < arguments.length(); ++i) {
            final char c = arguments.charAt(i);
            if (escape) {
                argument.append(c);
                escape = false;
                continue;
            }
            if (c == '\\') {
                escape = true;
                continue;
            }
            if (inQuote) {
                argument.append(c);
                if (c == '"') {
                    inQuote = false;
                }
            } else if (c == '"') {
                inQuote = true;
                argument.append(c);
            } else if (Character.isSpaceChar(c)) {
                if (argument.length() > 0) {
                    argumentsList.add(argument.toString());
                    argument.delete(0, argument.length());
                }
            } else {
                argument.append(c);
            }
        }
        argumentsList.add(argument.toString());
        return argumentsList;
    }
}
