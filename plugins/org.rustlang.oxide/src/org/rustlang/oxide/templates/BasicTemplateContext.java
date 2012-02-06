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
    public TemplateBuffer evaluate(Template template)
            throws BadLocationException, TemplateException {
        TemplateTranslator translator = new TemplateTranslator();
        TemplateBuffer buffer = translator.translate(template);
        getContextType().resolve(buffer, this);
        return buffer;
    }

    @Override
    public boolean canEvaluate(final Template template) {
        return true;
    }
}
