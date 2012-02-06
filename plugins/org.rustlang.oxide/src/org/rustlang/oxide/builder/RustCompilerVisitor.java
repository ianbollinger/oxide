package org.rustlang.oxide.builder;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.rustlang.oxide.OxidePlugin;

public class RustCompilerVisitor extends RustBuilderVisitor {
    @Override
    public boolean visit(final IResource resource) throws CoreException {
        //OxidePlugin.log(resource.toString());
        return true;
    }

    @Override
    public boolean visit(final IResourceDelta delta) throws CoreException {
        //OxidePlugin.log(delta.toString());
        return true;
    }
}
