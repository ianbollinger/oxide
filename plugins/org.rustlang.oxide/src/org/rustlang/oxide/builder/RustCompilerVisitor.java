package org.rustlang.oxide.builder;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.rustlang.oxide.common.EnumPreferenceStore;

public class RustCompilerVisitor extends RustBuilderVisitor {
    private final EnumPreferenceStore preferenceStore;

    public RustCompilerVisitor(final EnumPreferenceStore preferenceStore) {
        this.preferenceStore = preferenceStore;
    }

    @Override
    public boolean visit(final IResource resource) throws CoreException {
        return true;
    }

    @Override
    public boolean visit(final IResourceDelta delta) throws CoreException {
        return true;
    }
}
