package org.rustlang.oxide.templates;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.InputStream;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateVariable;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.jukito.MockProvider;
import org.jukito.TestScope;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rustlang.oxide.common.CommonModule;

@RunWith(JukitoRunner.class)
public class TemplateFileWriterTest {
    @Inject TemplateFileWriter writer;

    @Before
    public void setUp(@Assisted final TemplateContext context) throws Exception {
        when(context.evaluate(any(Template.class)))
                .thenReturn(new TemplateBuffer("", new TemplateVariable[] {}));
    }

    @Test
    public void shouldCreateFile() throws Exception {
        final IFile file = mock(IFile.class);
        writer.createFile(file, null, mock(IProgressMonitor.class));
        verify(file).create(any(InputStream.class), anyBoolean(),
                any(IProgressMonitor.class));
    }

    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            bindAnnotatedMock(TemplateContext.class);
            install(new CommonModule());
        }

        // TODO: move somewhere appropriate.
        private <T> void bindAnnotatedMock(final Class<T> clazz) {
            bind(clazz).annotatedWith(Assisted.class)
                    .toProvider(new MockProvider<T>(clazz))
                    .in(TestScope.SINGLETON);
        }
    }
}
