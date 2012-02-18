package org.rustlang.oxide.templates;

import org.eclipse.jface.text.templates.TemplateContextType;

public class RustTemplateContextType extends TemplateContextType {
    public RustTemplateContextType() {
        // Superclass has no public constructor.
    }

    public RustTemplateContextType(final String id) {
        super(id);
    }

    public RustTemplateContextType(final String id, final String name) {
        super(id, name);
    }
}
