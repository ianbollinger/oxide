package org.rustlang.oxide.templates;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.TemplateTranslator;

public class BasicTemplateContext extends TemplateContext {
    public BasicTemplateContext(final TemplateContextType contextType) {
        super(contextType);
    }

    @Override
    public TemplateBuffer evaluate(
            final Template template) throws TemplateException {
        final TemplateTranslator translator = new TemplateTranslator();
        final TemplateBuffer buffer = translator.translate(template);
        try {
            getContextType().resolve(buffer, this);
        } catch (final BadLocationException e) {
            throw new TemplateException(e);
        }
        return buffer;
    }

    @Override
    public boolean canEvaluate(
            @SuppressWarnings("unused") final Template template) {
        return true;
    }
}
