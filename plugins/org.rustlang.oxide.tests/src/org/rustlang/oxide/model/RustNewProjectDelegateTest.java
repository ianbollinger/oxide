package org.rustlang.oxide.model;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.sapphire.modeling.ProgressMonitor;
import org.eclipse.sapphire.modeling.Value;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.jukito.MockProvider;
import org.jukito.TestScope;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rustlang.oxide.templates.SapphireTemplateContextFactory;
import org.rustlang.oxide.wizards.PerspectiveUpdater;

// TODO: test error branches.
@RunWith(JukitoRunner.class)
public class RustNewProjectDelegateTest {
    @Inject RustNewProjectDelegate delegate;

    @Before
    public void setUp(@Assisted final RustProject model) {
        @SuppressWarnings("unchecked")
        final Value<String> valueMock = mock(Value.class);
        when(valueMock.getContent()).thenReturn("foo");
        // TODO: this is required due to a LoD violation.
        when(model.getProjectName())
                .thenReturn(valueMock);
    }

    @Before
    public void setUpFactory(
            final RustNewProjectOperationFactory factory) {
        // TODO: this is required due to a LoD violation.
        final RustNewProjectOperation operation =
                mock(RustNewProjectOperation.class);
        when(factory.create(
                        any(IProject.class),
                        any(IProjectDescription.class),
                        any(TemplateContext.class)))
                .thenReturn(operation);
    }

    @Test
    public void testExecute() {
        assertTrue(RustNewProjectDelegate.execute(null, null).ok());
    }

    @Test
    public void testRun(final PerspectiveUpdater perspectiveUpdater,
            @Assisted final IConfigurationElement configuration) {
        delegate.run();
        // TODO: write rest of test.
        verify(perspectiveUpdater).update(configuration);
    }

    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            bindAnnotatedMock(IConfigurationElement.class);
            bindAnnotatedMock(ProgressMonitor.class);
            bindAnnotatedMock(RustProject.class);
            bindMock(SapphireTemplateContextFactory.class)
                    .in(TestScope.SINGLETON);
            bindMock(PerspectiveUpdater.class).in(TestScope.SINGLETON);
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
