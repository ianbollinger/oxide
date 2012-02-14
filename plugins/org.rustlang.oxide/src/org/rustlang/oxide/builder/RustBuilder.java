package org.rustlang.oxide.builder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharStreams;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.rustlang.oxide.OxidePlugin;
import org.rustlang.oxide.common.Collections3;
import org.rustlang.oxide.common.EnumPreferenceStore;
import org.rustlang.oxide.preferences.RustPreferenceKey;

public class RustBuilder extends IncrementalProjectBuilder {
    public static final String ID = "org.rustlang.oxide.RustBuilder";

    @Override
    protected IProject[] build(final int kind,
            @SuppressWarnings("unused") final Map<String, String> args,
            final IProgressMonitor monitor) throws CoreException {
        if (kind == IncrementalProjectBuilder.CLEAN_BUILD) {
            cleanBuild(monitor);
        } else if (kind == IncrementalProjectBuilder.FULL_BUILD) {
            fullBuild(monitor);
        } else {
            final IResourceDelta delta = getDelta(getProject());
            if (delta == null) {
                fullBuild(monitor);
            } else {
                incrementalBuild(delta, monitor);
            }
        }
        final IProject project = getProject();
        project.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
        final EnumPreferenceStore store = OxidePlugin.getEnumPreferenceStore();
        final IResource crateFile = project.getFile(project.getName() + ".rc");
        rustc(crateFile, project.getProject().getLocation().toFile(), store);
        return null;
    }

    private void rustc(final IResource resource, final File workingDirectory,
            final EnumPreferenceStore preferenceStore) throws CoreException {
        final String file = resource.getProjectRelativePath().toOSString();
        // TODO: --warn-unused-imports
        final String compilerPath = preferenceStore
                .getString(RustPreferenceKey.COMPILER_PATH);
        final String libraryPaths = preferenceStore
                .getString(RustPreferenceKey.LIBRARY_PATHS);
        final Iterable<String> libraryPathList = Splitter.on(';')
                .omitEmptyStrings().split(libraryPaths);
        final ImmutableList.Builder<String> builder = ImmutableList.builder();
        builder.add(compilerPath); //.add("--no-trans");
        for (final String libraryPath : libraryPathList) {
            builder.add("-L").add(libraryPath);
        }
        builder.add(file);
        final String[] commandLine = Collections3.toArray(builder.build(),
                String.class);
        final Process process = DebugPlugin.exec(commandLine, workingDirectory);
        try {
            final InputStream inputStream = process.getInputStream();
            final Reader reader = new InputStreamReader(inputStream);
            parseLines(CharStreams.readLines(reader));
        } catch (IOException e) {
            OxidePlugin.log(e);
        }
    }

    private void parseLines(final List<String> lines)
            throws CoreException {
        final Pattern p = Pattern
                .compile("^([^:]+):(\\d+):(\\d+): (\\d+):(\\d+) error: (.+)$");
        for (final String line : lines) {
            final Matcher m = p.matcher(line);
            if (!m.matches()) {
                continue;
            }
            final IResource resource = getProject().getFile(m.group(1));
            final int lineStart = Integer.parseInt(m.group(2));
            final int columnStart = Integer.parseInt(m.group(3));
            final int lineEnd = Integer.parseInt(m.group(4));
            final int columnEnd = Integer.parseInt(m.group(5));
            final String message = m.group(6);
            reportError(resource, lineStart, columnStart, columnEnd, message);
        }
    }

    private void reportError(final IResource resource, final int line,
            final int start, final int end, final String msg)
            throws CoreException {
        final IMarker marker = resource.createMarker(IMarker.PROBLEM);
        marker.setAttribute(IMarker.LINE_NUMBER, line);
        // TODO: CHAR_START/END are relative to the beginning of the FILE
        // and not the LINE. Ugh.
        // marker.setAttribute(IMarker.CHAR_START, start);
        // marker.setAttribute(IMarker.CHAR_END, end);
        marker.setAttribute(IMarker.MESSAGE, msg);
        marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
        marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
    }
    
    @Override
    protected void clean(
            @SuppressWarnings("unused") final IProgressMonitor monitor)
            throws CoreException {
        // TOODO: implement
    }

    private void cleanBuild(final IProgressMonitor monitor)
            throws CoreException {
        clean(monitor);
        fullBuild(monitor);
    }

    private void fullBuild(final IProgressMonitor monitor) {
        final IProject project = getProject();
        try {
            for (final IResourceVisitor visitor : getVisitors()) {
                project.accept(visitor);
            }
        } catch (final CoreException e) {
            OxidePlugin.log(e);
        }
    }

    private List<RustBuilderVisitor> getVisitors() {
        final EnumPreferenceStore store = OxidePlugin.getEnumPreferenceStore();
        return ImmutableList.of((RustBuilderVisitor) new RustCompilerVisitor(
                store));
    }

    private void incrementalBuild(final IResourceDelta delta,
            final IProgressMonitor monitor) {
        try {
            for (final IResourceDeltaVisitor visitor : getVisitors()) {
                delta.accept(visitor);
            }
        } catch (final CoreException e) {
            OxidePlugin.log(e);
        }
    }
}
