/*
 * Copyright 2012 Ian D. Bollinger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
        for (final ModelProperty property : type.properties()) {
            if (property instanceof ValueProperty) {
                setVariable(model, (ValueProperty) property);
            }
        }
        return getTemplateContext();
    }

    private void setVariable(final IModelElement model,
            final ValueProperty property) {
        final Object value = model.read(property).getContent();
        final String stringValue = (value == null) ? "" : value.toString();
        getTemplateContext().setVariable(property.getName(), stringValue);
    }

    TemplateContext getTemplateContext() {
        return templateContext;
    }
}
