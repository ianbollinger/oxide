package org.rustlang.oxide.templates;

import static org.junit.Assert.assertTrue;
import com.google.inject.Inject;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateTranslator;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JukitoRunner.class)
public class BasicTemplateContextTest {
    @Inject BasicTemplateContext context;

    @Test
    public void testCanEvaluate() {
        assertTrue(context.canEvaluate(null));
    }

    @Test
    public void testEvaluate() throws Exception {
        context.evaluate(null);
        // TODO: assert something.
    }

    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            bindMock(TemplateContextType.class);
            bindMock(TemplateTranslator.class);
        }
    }
}
