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

package org.rustlang.oxide.builder;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import com.google.common.base.Charsets;
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
import org.rustlang.oxide.OxidePlugin;
import org.rustlang.oxide.common.EnumPreferenceStore;
import org.rustlang.oxide.common.launch.ProcessExecutor;
import org.rustlang.oxide.preference.RustPreferenceKey;
import org.slf4j.Logger;

/**
 * TODO: Document class.
 */
public class RustBuilder extends IncrementalProjectBuilder {
    /**
     * TODO: Document field.
     */
    public static final String ID = "org.rustlang.oxide.RustBuilder";

    private final EnumPreferenceStore preferenceStore;
    private final Pattern errorPattern;
    private final Logger logger;
    private final ProcessExecutor processExecutor;

    /**
     * The constructor is an implementation detail and should not be invoked
     * directly.
     */
    public RustBuilder() {
        // TODO: inject fields.
        this.preferenceStore = OxidePlugin.getEnumPreferenceStore();
        this.errorPattern = Pattern.compile(
                "^([^:]+):(\\d+):(\\d+): (\\d+):(\\d+) error: (.+)$");
        this.logger = OxidePlugin.getLogger();
        this.processExecutor = new ProcessExecutor();
    }

    @Override @Nullable
    protected IProject[] build(final int kind,
            @Nullable final Map<String, String> args,
            @Nullable final IProgressMonitor monitor) throws CoreException {
        decideOnAndExecuteBuild(kind, monitor);
        eraseProblemMarkers();
        final IProject project = getProject();
        executeRustCompiler(getCrateFile(project), getSourceFile(project));
        return null;
    }

    private File getSourceFile(final IProject project) {
        return project.getProject().getLocation().toFile();
    }

    private IResource getCrateFile(final IProject project) {
        return project.getFile(project.getName() + ".rc");
    }

    private void decideOnAndExecuteBuild(final int kind,
            @Nullable final IProgressMonitor monitor) throws CoreException {
        switch (kind) {
        case IncrementalProjectBuilder.CLEAN_BUILD:
            cleanBuild(monitor);
            break;
        case IncrementalProjectBuilder.FULL_BUILD:
            fullBuild();
            break;
        default:
            attemptIncrementalBuild();
        }
    }

    private void attemptIncrementalBuild() throws CoreException {
        final IResourceDelta delta = getDelta(getProject());
        if (delta == null) {
            fullBuild();
        } else {
            incrementalBuild(delta);
        }
    }

    private void eraseProblemMarkers() throws CoreException {
        final boolean includeSubtypes = true;
        getProject().deleteMarkers(IMarker.PROBLEM, includeSubtypes,
                IResource.DEPTH_INFINITE);
    }

    private void executeRustCompiler(final IResource resource,
            final File workingDirectory) throws CoreException {
        final Process process = processExecutor.execute(
                buildCommandLine(resource), workingDirectory);
        interpretCompilerOutput(process);
    }

    private void interpretCompilerOutput(
            final Process process) throws CoreException {
        try {
            // TODO: ensure encoding is correct.
            final Reader reader = new InputStreamReader(
                    process.getInputStream(), Charsets.UTF_8);
            parseLines(CharStreams.readLines(reader));
        } catch (final IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private List<String> buildCommandLine(final IResource resource) {
        final String file = resource.getProjectRelativePath().toOSString();
        // TODO: --warn-unused-imports
        final ImmutableList.Builder<String> builder =
                ImmutableList.<String>builder().add(getCompilerPath());
        // TODO: .add("--no-trans");
        for (final String libraryPath : getLibraryPaths()) {
            builder.add("-L").add(libraryPath);
        }
        return builder.add(file).build();
    }

    private String getCompilerPath() {
        return preferenceStore.getString(RustPreferenceKey.COMPILER_PATH);
    }

    private Iterable<String> getLibraryPaths() {
        final String libraryPaths = preferenceStore
                .getString(RustPreferenceKey.LIBRARY_PATHS);
        return Splitter.on(';').omitEmptyStrings().split(libraryPaths);
    }

    // TODO: rename!
    private void parseLines(final List<String> lines) throws CoreException {
        for (final String line : lines) {
            parseLine(line);
        }
    }

    // TODO: rename!
    private void parseLine(final String line) throws CoreException {
        final Matcher m = errorPattern.matcher(line);
        if (m.matches()) {
            final IResource resource = getProject().getFile(m.group(1));
            final int lineStart = Integer.parseInt(m.group(2));
            // final int columnStart = Integer.parseInt(m.group(3));
            // final int lineEnd = Integer.parseInt(m.group(4));
            // final int columnEnd = Integer.parseInt(m.group(5));
            final String message = m.group(6);
            reportError(resource, lineStart, message);
        }
    }

    private void reportError(final IResource resource, final int line,
            final String msg) throws CoreException {
        // TODO: inject factory.
        final IMarker marker = resource.createMarker(IMarker.PROBLEM);
        marker.setAttribute(IMarker.LINE_NUMBER, line);
        // marker.setAttribute(IMarker.CHAR_START, start);
        // marker.setAttribute(IMarker.CHAR_END, end);
        marker.setAttribute(IMarker.MESSAGE, msg);
        marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
        marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
    }

    private void cleanBuild(
            @Nullable final IProgressMonitor monitor) throws CoreException {
        clean(monitor);
        fullBuild();
    }

    private void fullBuild() throws CoreException {
        final IProject project = getProject();
        for (final IResourceVisitor visitor : getVisitors()) {
            project.accept(visitor);
        }
    }

    private List<? extends RustBuilderVisitor> getVisitors() {
        // TODO: inject.
        return ImmutableList.of(new RustCompilerVisitor());
    }

    private void incrementalBuild(
            final IResourceDelta delta) throws CoreException {
        for (final IResourceDeltaVisitor visitor : getVisitors()) {
            delta.accept(visitor);
        }
    }
}
