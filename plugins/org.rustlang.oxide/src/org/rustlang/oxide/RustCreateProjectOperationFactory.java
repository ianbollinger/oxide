package org.rustlang.oxide;

import java.net.URI;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.templates.TemplateContext;

public interface RustCreateProjectOperationFactory {
    RustCreateProjectOperation create(IProject project, URI location,
            List<IProject> referencedProjects, TemplateContext templateContext);
}
