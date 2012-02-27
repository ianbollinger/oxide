package org.rustlang.oxide.templates;

import com.google.inject.Inject;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JukitoRunner.class)
public class SapphireTemplateContextFactoryTest {
    @Inject SapphireTemplateContextFactory factory;
    private ModelElementType type;

    @Before
    public void setUp() {
        type = new ModelElementType(Object.class);
    }

    @Test
    public void testCreate() {
        factory.create(type, null);
        // TODO: assert something.
    }

    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
        }
    }
}
