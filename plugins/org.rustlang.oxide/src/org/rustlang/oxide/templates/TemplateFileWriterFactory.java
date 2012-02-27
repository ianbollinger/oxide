package org.rustlang.oxide.templates;

import org.eclipse.jface.text.templates.TemplateContext;

public interface TemplateFileWriterFactory {
    TemplateFileWriter create(TemplateContext context);
}
