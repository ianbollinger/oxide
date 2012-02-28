package org.rustlang.oxide.templates;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
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
import org.rustlang.oxide.common.SubProgressMonitorFactory;
import org.slf4j.Logger;

public class TemplateFileWriter {
    private final TemplateStore templateStore;
    private final SubProgressMonitorFactory subProgressMonitorFactory;
    private final Charset charset;
    private final Logger logger;
    private final TemplateContext templateContext;

    @Inject
    TemplateFileWriter(final TemplateStore templateStore,
            final SubProgressMonitorFactory subProgressMonitorFactory,
            final Charset charset,
            final Logger logger,
            @Assisted final TemplateContext templateContext) {
        this.templateStore = templateStore;
        this.subProgressMonitorFactory = subProgressMonitorFactory;
        this.charset = charset;
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
            final byte[] bytes = buffer.getString().getBytes(charset);
            return new ByteArrayInputStream(bytes);
        } catch (final BadLocationException e) {
            logger.error(e.getMessage(), e);
        } catch (final TemplateException e) {
            logger.error(e.getMessage(), e);
        }
        return new ByteArrayInputStream(new byte[] {});
    }
}
