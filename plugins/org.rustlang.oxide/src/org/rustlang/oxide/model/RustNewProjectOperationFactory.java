package org.rustlang.oxide.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.rustlang.oxide.templates.TemplateFileWriter;

public interface RustNewProjectOperationFactory {
    RustNewProjectOperation create(IProject project,
            TemplateFileWriter factory, IProjectDescription description);
}
