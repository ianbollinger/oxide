package org.rustlang.oxide;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class OxideLogger {
    public void log(final Throwable throwable) {
        log(null, throwable);
    }

    public void log(final String message) {
        log(message, null);
    }

    public void log(final String message, final Throwable throwable) {
        log(IStatus.INFO, message, throwable);
    }

    public void log(final int severity, final String message,
            final Throwable throwable) {
        final Status status = new Status(severity, OxidePlugin.ID, IStatus.OK,
                message, throwable);
        // TODO: eliminate circular dependency.
        OxidePlugin.getDefault().getLog().log(status);
    }
}
