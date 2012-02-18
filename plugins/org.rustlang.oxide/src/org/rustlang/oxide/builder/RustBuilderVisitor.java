package org.rustlang.oxide.builder;

import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;

public interface RustBuilderVisitor extends IResourceVisitor,
        IResourceDeltaVisitor {
}
