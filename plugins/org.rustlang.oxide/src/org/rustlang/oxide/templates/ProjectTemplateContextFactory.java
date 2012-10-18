package org.rustlang.oxide.templates;

import org.eclipse.jface.text.templates.TemplateContext;
import org.rustlang.oxide.OxidePlugin;
import org.rustlang.oxide.model.RustProject;

public class ProjectTemplateContextFactory {
    public TemplateContext create(final RustProject project) {
        final TemplateContext templateContext = OxidePlugin.getInstance(
                BasicTemplateContext.class);
        templateContext.setVariable("project_name", project.getProjectName());
        templateContext.setVariable("crate_type", project.getType().toString());
        templateContext.setVariable("version", project.getVersion());
        templateContext.setVariable("uuid", project.getUuid().toString());
        templateContext.setVariable("url", project.getUrl().toString());
        templateContext.setVariable("author", project.getAuthor());
        templateContext.setVariable("license", project.getLicense());
        templateContext.setVariable("brief", project.getBrief());
        templateContext.setVariable("desc", project.getDescription());
        return templateContext;
    }
}
