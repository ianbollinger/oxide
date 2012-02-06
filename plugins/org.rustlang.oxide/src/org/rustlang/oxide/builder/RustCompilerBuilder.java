package org.rustlang.oxide.builder;

import java.util.List;
import java.util.Map;
import com.google.common.collect.ImmutableList;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.rustlang.oxide.OxidePlugin;

public class RustCompilerBuilder extends IncrementalProjectBuilder {
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
        return null;
    }

    @Override
    protected void startupOnInitialize() {
        super.startupOnInitialize();
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

    public List<RustBuilderVisitor> getVisitors() {
        return ImmutableList.of((RustBuilderVisitor) new RustCompilerVisitor());
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
