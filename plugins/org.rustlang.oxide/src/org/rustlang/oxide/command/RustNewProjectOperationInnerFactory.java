package org.rustlang.oxide.command;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.IConfigurationElement;
import org.rustlang.oxide.templates.TemplateFileWriter;

public interface RustNewProjectOperationInnerFactory {
    RustNewProjectOperation create(
            IConfigurationElement configuration, IProject project,
            TemplateFileWriter writer, IProjectDescription description);
}
