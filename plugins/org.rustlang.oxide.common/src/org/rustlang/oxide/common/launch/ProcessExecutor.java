package org.rustlang.oxide.common.launch;

import java.io.File;
import javax.annotation.concurrent.Immutable;
import com.google.common.collect.Iterables;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;

/**
 * TODO: Document class.
 */
@Immutable
public class ProcessExecutor {
    // FIXME: do not throw checked exceptions in public API.
    /**
     * TODO: Document method.
     *
     * @param commandLine
     * @param workingDirectory
     * @return
     * @throws CoreException
     */
    public Process execute(final Iterable<String> commandLine,
            final File workingDirectory) throws CoreException {
        final String[] commandLineArray = Iterables.toArray(commandLine,
                String.class);
        return DebugPlugin.exec(commandLineArray, workingDirectory);
    }

    /**
     * TODO: Document method.
     *
     * @param launch
     * @param process
     * @param executableName
     */
    public void newProcess(final ILaunch launch, final Process process,
            final String executableName) {
        DebugPlugin.newProcess(launch, process, executableName);
    }
}
