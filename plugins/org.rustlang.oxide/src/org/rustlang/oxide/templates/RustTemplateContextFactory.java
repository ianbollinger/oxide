package org.rustlang.oxide.templates;

import com.google.inject.Inject;
import org.eclipse.jface.text.templates.TemplateContext;
import org.rustlang.oxide.model.RustProject;

public class RustTemplateContextFactory {
    private final TemplateContext templateContext;

    @Inject
    RustTemplateContextFactory(final TemplateContext templateContext) {
        this.templateContext = templateContext;
    }

    public TemplateContext create(final RustProject model) {
        // TODO: provide a generic solution.
        templateContext.setVariable("project_name", model.getCrateName()
                .getContent());
        templateContext.setVariable("crate_type", model.getType().getContent()
                .getValue());
        templateContext.setVariable("version", model.getVersion().getContent());
        templateContext.setVariable("uuid", model.getUuid().toString());
        templateContext.setVariable("url", model.getUrl().getContent()
                .toString());
        templateContext.setVariable("author", model.getAuthor().getContent());
        templateContext.setVariable("license", model.getLicense().getContent());
        templateContext.setVariable("brief", model.getBrief().getContent());
        templateContext
                .setVariable("desc", model.getDescription().getContent());
        return templateContext;
    }
}
