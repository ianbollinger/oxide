package org.rustlang.oxide.templates;

import com.google.inject.Inject;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.ModelProperty;
import org.eclipse.sapphire.modeling.ValueProperty;

public class SapphireTemplateContextFactory {
    private final TemplateContext templateContext;

    @Inject
    SapphireTemplateContextFactory(final TemplateContext templateContext) {
        this.templateContext = templateContext;
    }

    public TemplateContext create(final ModelElementType type,
            final IModelElement model) {
        for (final ModelProperty property : type.getProperties()) {
            if (property instanceof ValueProperty) {
                setVariable(model, (ValueProperty) property);
            }
        }
        return templateContext;
    }

    private void setVariable(final IModelElement model,
            final ValueProperty property) {
        final Object value = model.read(property).getContent();
        // TODO: this falsely assumes that any value can be deserialized
        // by calling toString.
        final String stringValue = (value == null) ? "" : value.toString();
        templateContext.setVariable(property.getName(), stringValue);
    }
}
