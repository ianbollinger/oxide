package org.rustlang.oxide.command;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.sapphire.modeling.ProgressMonitor;
import org.eclipse.sapphire.modeling.Value;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.jukito.MockProvider;
import org.jukito.TestScope;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rustlang.oxide.command.RustNewProjectOperation;
import org.rustlang.oxide.command.RustNewProjectOperationFactory;
import org.rustlang.oxide.command.RustNewProjectOperationInnerFactory;
import org.rustlang.oxide.model.RustProject;
import org.rustlang.oxide.templates.SapphireTemplateContextFactory;
import org.rustlang.oxide.templates.TemplateFileWriter;

// TODO: test error branches.
@RunWith(JukitoRunner.class)
public class RustNewProjectOperationFactoryTest {
    @Inject RustNewProjectOperationFactory factory;

    @Before
    public void setUpFactory(
            final RustNewProjectOperationInnerFactory innerFactory) {
        // TODO: this is required due to a LoD violation.
        final RustNewProjectOperation operation =
                mock(RustNewProjectOperation.class);
        when(innerFactory.create(
                        any(IConfigurationElement.class),
                        any(IProject.class),
                        any(TemplateFileWriter.class),
                        any(IProjectDescription.class)))
                .thenReturn(operation);
    }

    @Test
    public void testCreate() {
        final RustProject project = mock(RustProject.class);
        @SuppressWarnings("unchecked")
        final Value<String> valueMock = mock(Value.class);
        // TODO: this is required due to a LoD violation.
        when(project.getProjectName())
                .thenReturn(valueMock);
        factory.create(project, null);
        // TODO: write rest of test.
    }

    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            bindAnnotatedMock(IConfigurationElement.class);
            bindAnnotatedMock(ProgressMonitor.class);
            bindAnnotatedMock(RustProject.class);
            bindMock(SapphireTemplateContextFactory.class)
                    .in(TestScope.SINGLETON);
        }

        // TODO: move somewhere appropriate.
        private <T> void bindAnnotatedMock(final Class<T> clazz) {
            bind(clazz)
                    .annotatedWith(Assisted.class)
                    .toProvider(new MockProvider<T>(clazz))
                    .in(TestScope.SINGLETON);
        }
    }
}
