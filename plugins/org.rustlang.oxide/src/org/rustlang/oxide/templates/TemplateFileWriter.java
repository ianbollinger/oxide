package org.rustlang.oxide.templates;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import com.google.common.base.Charsets;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateException;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.rustlang.oxide.OxideLogger;
import org.rustlang.oxide.common.SubProgressMonitorFactory;

public class TemplateFileWriter {
    private final TemplateStore templateStore;
    private final SubProgressMonitorFactory subProgressMonitorFactory;
    private final OxideLogger logger;
    private final TemplateContext templateContext;

    @Inject
    TemplateFileWriter(final TemplateStore templateStore,
            final SubProgressMonitorFactory subProgressMonitorFactory,
            final OxideLogger logger,
            @Assisted final TemplateContext templateContext) {
        this.templateStore = templateStore;
        this.subProgressMonitorFactory = subProgressMonitorFactory;
        this.logger = logger;
        this.templateContext = templateContext;
    }

    public void createFile(final IFile file, final String templateName,
            final IProgressMonitor monitor) throws CoreException {
        final boolean force = false;
        file.create(getInputStream(templateName), force,
                subProgressMonitorFactory.create(monitor));
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
    }

    private InputStream getInputStream(final String name) {
        // TODO: replace with findTeplateById
        final Template template = templateStore.findTemplate(name);
        try {
            final TemplateBuffer buffer = templateContext.evaluate(template);
            // TODO: remove LoD violation.
            final byte[] bytes = buffer.getString().getBytes(Charsets.UTF_8);
            return new ByteArrayInputStream(bytes);
        } catch (final BadLocationException e) {
            logger.log(e);
        } catch (final TemplateException e) {
            logger.log(e);
        }
        return new ByteArrayInputStream(new byte[] {});
    }
}
