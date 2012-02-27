package org.rustlang.oxide.templates;

import com.google.inject.Inject;
import org.eclipse.jface.text.templates.TemplateContextType;

// TODO: is this class actually needed?
public class RustTemplateContextType extends TemplateContextType {
    public static final String ID =
            "org.rustlang.oxide.templates.RustTemplateContextType";

    @Inject
    RustTemplateContextType() {
        super(ID);
    }
}
