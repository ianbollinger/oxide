package org.rustlang.oxide.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.templates.TemplateContext;

public interface RustNewProjectOperationFactory {
    RustNewProjectOperation create(IProject project,
            TemplateContext templateContext);
}
