package org.rustlang.oxide;

import com.google.inject.Inject;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class OxideLogger {
    private final ILog wrappedLog;

    @Inject
    OxideLogger(final ILog wrappedLog) {
        this.wrappedLog = wrappedLog;
    }

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
        // TODO: get this from a factory.
        final Status status = new Status(severity, OxidePlugin.ID, IStatus.OK,
                message, throwable);
        wrappedLog.log(status);
    }
}
