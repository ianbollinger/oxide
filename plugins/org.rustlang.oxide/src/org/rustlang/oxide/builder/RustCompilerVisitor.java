package org.rustlang.oxide.builder;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;

public class RustCompilerVisitor implements RustBuilderVisitor {
    @Override
    public boolean visit(@SuppressWarnings("unused") final IResource resource) {
        return true;
    }

    @Override
    public boolean visit(
            @SuppressWarnings("unused") final IResourceDelta delta) {
        return true;
    }
}
