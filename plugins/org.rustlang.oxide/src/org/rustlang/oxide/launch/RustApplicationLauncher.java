package org.rustlang.oxide.launch;

import java.io.File;
import java.util.List;
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
    public boolean buildForLaunch(final ILaunchConfiguration configuration,
            @SuppressWarnings("unused") final String mode,
            @SuppressWarnings("unused")
            final IProgressMonitor monitor) throws CoreException {
        final String project = configuration.getAttribute(
                RustLaunchAttribute.PROJECT.toString(), "");
        final String executable = configuration.getAttribute(
                RustLaunchAttribute.EXECUTABLE.toString(), "");
        return !project.isEmpty() && !executable.isEmpty();
    }

    @Override
    public boolean finalLaunchCheck(
            @SuppressWarnings("unused") final ILaunchConfiguration config,
            @SuppressWarnings("unused") final String mode,
            @SuppressWarnings("unused") final IProgressMonitor monitor) {
        return true;
    }

    @Override
    public ILaunch getLaunch(final ILaunchConfiguration configuration,
            final String mode) {
        // TODO: make a factory.
        return new Launch(configuration, mode, null);
    }

    @Override
    public boolean preLaunchCheck(
            @SuppressWarnings("unused")
            final ILaunchConfiguration configuration,
            @SuppressWarnings("unused") final String mode,
            @SuppressWarnings("unused") final IProgressMonitor monitor) {
        return true;
    }

    @Override
    public void launch(final ILaunchConfiguration configuration,
            @SuppressWarnings("unused") final String mode, final ILaunch launch,
            @SuppressWarnings("unused")
            final IProgressMonitor monitor) throws CoreException {
        execute(configuration, launch);
    }

    private IProject getProject(final String projectName) throws CoreException {
        final IResource resource = workspaceRoot.findMember(projectName);
        if (resource instanceof IProject) {
            final IProject project = (IProject) resource;
            if (isOpenRustProject(project)) {
                return project;
            }
        }
        // TODO: let's not return null.
        return null;
    }

    private boolean isOpenRustProject(
            final IProject project) throws CoreException {
        return project.isOpen() && project.hasNature(RustNature.ID);
    }

    private void execute(final ILaunchConfiguration configuration,
            final ILaunch launch) throws CoreException {
        final String projectName = configuration.getAttribute(
                RustLaunchAttribute.PROJECT.toString(), "");
        final IProject project = getProject(projectName);
        if (project == null) {
            return;
        }
        final String executable = configuration.getAttribute(
                RustLaunchAttribute.EXECUTABLE.toString(), "");
        final String programArguments = configuration.getAttribute(
                RustLaunchAttribute.ARGUMENTS.toString(), "");
        final IPath relativeExecutablePath = Path.fromOSString(executable);
        final IPath projectPath = project.getLocation();
        final String executableName = relativeExecutablePath.lastSegment();
        final IPath executablePath = getExecutablePath(executableName);
        final File workingDirectory = new File(projectPath.toOSString());
        final String commandLine = projectPath.append(executablePath)
                .toOSString();
        final List<String> arguments = Lists.newArrayList();
        arguments.add(commandLine);
        arguments.addAll(argumentsAsList(programArguments));
        final String[] array = Iterables.toArray(arguments,
                String.class);
        spawnProcess(launch, executableName, workingDirectory, array);
    }

    // TODO: create a strategy for this.
    void spawnProcess(final ILaunch launch, final String executableName,
            final File workingDirectory,
            final String[] array) throws CoreException {
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
    private List<String> argumentsAsList(final String arguments) {
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
