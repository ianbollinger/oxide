package org.rustlang.oxide.templates;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import com.google.inject.Inject;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateTranslator;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.jukito.TestScope;
import org.junit.Test;
import org.junit.runner.RunWith;

// TODO: test error branch.
@RunWith(JukitoRunner.class)
public class BasicTemplateContextTest {
    @Inject BasicTemplateContext context;

    @Test
    public void testCanEvaluate() {
        assertTrue(context.canEvaluate(null));
    }

    @Test
    public void testEvaluate(final TemplateContextType type) throws Exception {
        final TemplateBuffer buffer = context.evaluate(null);
        verify(type).resolve(buffer, context);
    }

    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            bindMock(TemplateContextType.class).in(TestScope.SINGLETON);
            bindMock(TemplateTranslator.class).in(TestScope.SINGLETON);
        }
    }
}
