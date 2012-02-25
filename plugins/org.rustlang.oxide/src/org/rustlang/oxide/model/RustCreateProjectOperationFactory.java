package org.rustlang.oxide.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.templates.TemplateContext;

public interface RustCreateProjectOperationFactory {
    RustCreateProjectOperation create(IProject project,
            TemplateContext templateContext);
}
