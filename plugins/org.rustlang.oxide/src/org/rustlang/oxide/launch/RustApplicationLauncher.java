package org.rustlang.oxide.launch;

import java.io.File;
import java.util.List;
import com.google.common.collect.Lists;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
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
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.rustlang.oxide.OxidePlugin;
import org.rustlang.oxide.common.Collections3;
import org.rustlang.oxide.nature.RustNature;

public class RustApplicationLauncher implements ILaunchConfigurationDelegate2 {
    public static final String ID = "org.rustlang.oxide.launch.configuration";

    @Override
    public boolean buildForLaunch(final ILaunchConfiguration configuration,
            @SuppressWarnings("unused") final String mode,
            @SuppressWarnings("unused") final IProgressMonitor monitor)
            throws CoreException {
        final String project = configuration.getAttribute(
                RustLaunchAttribute.PROJECT.toString(), "");
        final String executable = configuration.getAttribute(
                RustLaunchAttribute.EXECUTABLE.toString(), "");
        return !project.isEmpty() && !executable.isEmpty();
    }

    @Override
    public boolean finalLaunchCheck(
            @SuppressWarnings("unused") final ILaunchConfiguration configuration,
            @SuppressWarnings("unused") final String mode,
            @SuppressWarnings("unused") final IProgressMonitor monitor) {
        return true;
    }

    @Override
    public ILaunch getLaunch(final ILaunchConfiguration configuration,
            final String mode) {
        return new Launch(configuration, mode, null);
    }

    @Override
    public boolean preLaunchCheck(
            @SuppressWarnings("unused") final ILaunchConfiguration configuration,
            @SuppressWarnings("unused") final String mode,
            @SuppressWarnings("unused") final IProgressMonitor monitor) {
        return true;
    }

    @Override
    public void launch(final ILaunchConfiguration configuration,
            @SuppressWarnings("unused") final String mode,
            final ILaunch launch,
            @SuppressWarnings("unused") final IProgressMonitor monitor)
            throws CoreException {
        execute(configuration, launch);
    }

    private IProject getProject(final String projectName) {
        final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        final IResource resource = root.findMember(projectName);
        try {
            if (resource instanceof IProject) {
                final IProject project = (IProject) resource;
                if (project.isOpen() && project.hasNature(RustNature.ID)) {
                    return project;
                }
            }
        } catch (final CoreException e) {
            OxidePlugin.getLogger().log(e);
        }
        return null;
    }

    private void execute(final ILaunchConfiguration configuration,
            final ILaunch launch) throws CoreException {
        final String projectName = configuration.getAttribute(
                RustLaunchAttribute.PROJECT.toString(), "");
        final String executable = configuration.getAttribute(
                RustLaunchAttribute.EXECUTABLE.toString(), "");
        final String programArguments = configuration.getAttribute(
                RustLaunchAttribute.ARGUMENTS.toString(), "");
        final IProject project = getProject(projectName);
        if (project != null) {
            final IPath relativeExecutablePath = Path.fromOSString(executable);
            final IPath projectPath = project.getLocation();
            final String executableName = relativeExecutablePath.lastSegment();
            IPath executablePath;
            if (!Util.isWindows()) {
                executablePath = Path.fromOSString(".").append(executableName);
            } else {
                executablePath = Path.fromOSString(executableName);
            }
            final String commandLine = projectPath.append(executablePath)
                    .toOSString();
            final String workingDirectory = projectPath.toOSString();
            final List<String> arguments = Lists.newArrayList();
            arguments.add(commandLine);
            final List<String> argumentsList = argumentsAsList(programArguments);
            if (argumentsList != null) {
                arguments.addAll(argumentsList);
            }
            final Process process = DebugPlugin.exec(Collections3.toArray(
                    arguments, String.class), new File(workingDirectory));
            DebugPlugin.newProcess(launch, process, executableName);
        }
    }

    public MessageConsole findMessageConsole(final String name) {
        final ConsolePlugin plugin = ConsolePlugin.getDefault();
        final IConsoleManager manager = plugin.getConsoleManager();
        final IConsole[] existing = manager.getConsoles();
        for (final IConsole element : existing) {
            if (name.equals(element.getName())) {
                return (MessageConsole) element;
            }
        }
        final MessageConsole console = new MessageConsole(name, null);
        manager.addConsoles(new IConsole[] { console });
        return console;
    }

    // TODO: move this somewhere appropriate.
    private List<String> argumentsAsList(final String arguments) {
        final List<String> argumentsList = Lists.newArrayList();
        if (arguments == null || arguments.trim().length() == 0) {
            return argumentsList;
        }
        boolean inQuote = false;
        boolean escape = false;
        final StringBuilder arg = new StringBuilder();
        int i = 0;
        while (i < arguments.length()) {
            final char c = arguments.charAt(i++);
            if (escape) {
                arg.append(c);
                escape = false;
                continue;
            }
            if (c == '\\') {
                escape = true;
                continue;
            }
            if (inQuote) {
                arg.append(c);
                if (c == '"') {
                    inQuote = false;
                }
            } else {
                if (c == '"') {
                    inQuote = true;
                    arg.append(c);
                } else {
                    if (Character.isSpaceChar(c)) {
                        if (arg.length() > 0) {
                            argumentsList.add(arg.toString());
                            arg.delete(0, arg.length());
                        }
                    } else {
                        arg.append(c);
                    }
                }
            }
        }
        argumentsList.add(arg.toString());
        return argumentsList.size() == 0 ? null : argumentsList;
    }
}
