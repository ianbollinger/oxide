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
import java.io.InputStream;
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
import com.google.common.collect.Iterables;
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
import org.rustlang.oxide.common.EnumPreferenceStore;
import org.rustlang.oxide.preferences.RustPreferenceKey;
import org.slf4j.Logger;

public class RustBuilder extends IncrementalProjectBuilder {
    public static final String ID = "org.rustlang.oxide.RustBuilder";
    private final EnumPreferenceStore preferenceStore;
    private final Logger logger;

    public RustBuilder() {
        // TODO: inject.
        this.preferenceStore = OxidePlugin.getEnumPreferenceStore();
        this.logger = OxidePlugin.getLogger();
    }

    @Override
    @Nullable
    protected IProject[] build(final int kind,
            @SuppressWarnings("unused") @Nullable
            final Map<String, String> args,
            @SuppressWarnings("null")
            final IProgressMonitor monitor) throws CoreException {
        if (kind == IncrementalProjectBuilder.CLEAN_BUILD) {
            cleanBuild(monitor);
        } else if (kind == IncrementalProjectBuilder.FULL_BUILD) {
            fullBuild();
        } else {
            final IResourceDelta delta = getDelta(getProject());
            if (delta == null) {
                fullBuild();
            } else {
                incrementalBuild(delta);
            }
        }
        final IProject project = getProject();
        final boolean includeSubtypes = true;
        project.deleteMarkers(IMarker.PROBLEM, includeSubtypes,
                IResource.DEPTH_INFINITE);
        final IResource crateFile = project.getFile(project.getName() + ".rc");
        final File file = project.getProject().getLocation().toFile();
        assert crateFile != null && file != null;
        rustc(crateFile, file);
        // Returning null is part of this method's contract, unfortunately.
        return null;
    }

    private void rustc(final IResource resource,
            final File workingDirectory) throws CoreException {
        final String file = resource.getProjectRelativePath().toOSString();
        assert file != null;
        // TODO: --warn-unused-imports
        final String compilerPath = preferenceStore
                .getString(RustPreferenceKey.COMPILER_PATH);
        final String libraryPaths = preferenceStore
                .getString(RustPreferenceKey.LIBRARY_PATHS);
        final Iterable<String> libraryPathList = Splitter.on(';')
                .omitEmptyStrings().split(libraryPaths);
        final ImmutableList.Builder<String> builder = ImmutableList.builder();
        builder.add(compilerPath);
        // TODO: .add("--no-trans");
        for (final String libraryPath : libraryPathList) {
            assert libraryPath != null;
            builder.add("-L").add(libraryPath);
        }
        builder.add(file);
        final String[] commandLine = Iterables.toArray(builder.build(),
                String.class);
        // TODO: eliminate static call.
        final Process process = DebugPlugin.exec(commandLine, workingDirectory);
        try {
            final InputStream inputStream = process.getInputStream();
            // TODO: ensure encoding is correct.
            final Reader reader = new InputStreamReader(inputStream,
                    Charsets.UTF_8);
            parseLines(CharStreams.readLines(reader));
        } catch (final IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void parseLines(final List<String> lines) throws CoreException {
        // TOD: inject
        final Pattern p = Pattern
                .compile("^([^:]+):(\\d+):(\\d+): (\\d+):(\\d+) error: (.+)$");
        for (final String line : lines) {
            final Matcher m = p.matcher(line);
            if (!m.matches()) {
                continue;
            }
            final IResource resource = getProject().getFile(m.group(1));
            final int lineStart = Integer.parseInt(m.group(2));
            // final int columnStart = Integer.parseInt(m.group(3));
            // final int lineEnd = Integer.parseInt(m.group(4));
            // final int columnEnd = Integer.parseInt(m.group(5));
            final String message = m.group(6);
            assert resource != null && message != null;
            reportError(resource, lineStart, message);
        }
    }

    private void reportError(final IResource resource, final int line,
            final String msg) throws CoreException {
        final IMarker marker = resource.createMarker(IMarker.PROBLEM);
        marker.setAttribute(IMarker.LINE_NUMBER, line);
        // marker.setAttribute(IMarker.CHAR_START, start);
        // marker.setAttribute(IMarker.CHAR_END, end);
        marker.setAttribute(IMarker.MESSAGE, msg);
        marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
        marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
    }

    private void cleanBuild(
            final IProgressMonitor monitor) throws CoreException {
        clean(monitor);
        fullBuild();
    }

    private void fullBuild() {
        final IProject project = getProject();
        try {
            for (final IResourceVisitor visitor : getVisitors()) {
                project.accept(visitor);
            }
        } catch (final CoreException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private List<? extends RustBuilderVisitor> getVisitors() {
        return ImmutableList.of(new RustCompilerVisitor());
    }

    private void incrementalBuild(final IResourceDelta delta) {
        try {
            for (final IResourceDeltaVisitor visitor : getVisitors()) {
                delta.accept(visitor);
            }
        } catch (final CoreException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
