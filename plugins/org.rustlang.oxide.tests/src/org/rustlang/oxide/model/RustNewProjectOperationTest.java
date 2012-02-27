package org.rustlang.oxide.model;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.templates.TemplateContext;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.jukito.MockProvider;
import org.jukito.TestScope;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.rustlang.oxide.nature.RustNature;
import org.rustlang.oxide.templates.TemplateFileWriter;
import org.rustlang.oxide.wizards.PerspectiveUpdater;

// TODO: verify file creation.
// TODO: cover error branches.
@RunWith(JukitoRunner.class)
public class RustNewProjectOperationTest {
    @Inject RustNewProjectOperation operation;
    @Inject IProgressMonitor monitor;

    @Before
    public void setUp(@Assisted final IProjectDescription description) {
        when(description.getNatureIds()).thenReturn(new String[] {});
    }

    @Test
    public void testExecute() {
        assertTrue(RustNewProjectOperation.execute(null, null).ok());
    }
    
    @Test
    public void shouldBeginMonitorTask() throws Exception {
        operation.execute(monitor);
        verify(monitor).done();
    }

    @Test
    public void shouldCloseMonitor() throws Exception {
        operation.execute(monitor);
        // TODO: validate arguments.
        verify(monitor).beginTask(anyString(), anyInt());
    }

    @Test
    public void shouldCreateProject(
            @Assisted final IProject project) throws Exception {
        operation.execute(monitor);
        verify(project).open(eq(IResource.BACKGROUND_REFRESH),
                Matchers.<IProgressMonitor>any());
    }

    @Test
    public void shouldOpenProject(
            @Assisted final IProject project) throws Exception {
        operation.execute(monitor);
        verify(project).open(eq(IResource.BACKGROUND_REFRESH),
                Matchers.<IProgressMonitor>any());
    }

    @Test
    public void shouldSetNatureIds(
            @Assisted final IProjectDescription description) throws Exception {
        operation.execute(monitor);
        verify(description).setNatureIds(new String[] { RustNature.ID });
    }

    @Test
    public void shouldSetProjectDescription(
            @Assisted final IProjectDescription description,
            @Assisted final IProject project) throws Exception {
        operation.execute(monitor);
        verify(project).setDescription(eq(description),
                Matchers.<IProgressMonitor>any());
    }

    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            bindAnnotatedMock(IProject.class);
            bindAnnotatedMock(IProjectDescription.class);
            bindAnnotatedMock(TemplateFileWriter.class);
            bindAnnotatedMock(TemplateContext.class);
            bindAnnotatedMock(IConfigurationElement.class);
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
