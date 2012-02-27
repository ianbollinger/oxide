package org.rustlang.oxide.common;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;

public class SubProgressMonitorFactory {
    private final int workScale;

    @Inject
    SubProgressMonitorFactory(@WorkScale final int workScale) {
        this.workScale = workScale;
    }

    public IProgressMonitor create(final IProgressMonitor monitor) {
        return new SubProgressMonitor(monitor, workScale);
    }

    public int getWorkScale() {
        return workScale;
    }

    @BindingAnnotation @Target({FIELD, METHOD, PARAMETER}) @Retention(RUNTIME)
    public @interface WorkScale {}
}
