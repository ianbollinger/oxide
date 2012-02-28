package org.rustlang.oxide.common;

import com.google.inject.Inject;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.rustlang.oxide.OxidePlugin;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

public class EclipseLogger extends MarkerIgnoringBase {
    private static final long serialVersionUID = 1L;
    private final ILog wrappedLog;

    @Inject
    EclipseLogger(final ILog wrappedLog) {
        this.wrappedLog = wrappedLog;
    }

    private void log(final int severity, final String message,
            final Throwable throwable) {
        wrappedLog.log(provideStatus(severity, message, throwable));
    }

    Status provideStatus(final int severity, final String message,
            final Throwable throwable) {
        // TODO: get this from a factory.
        return new Status(severity, OxidePlugin.ID, IStatus.OK, message,
                throwable);
    }

    private void formatAndLog(final int severity, final String format,
            final Object arg1, final Object arg2) {
        final FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
        log(severity, tp.getMessage(), tp.getThrowable());
    }

    private void formatAndLog(final int severity, final String format,
            final Object[] args) {
        final FormattingTuple tp = MessageFormatter.arrayFormat(format, args);
        log(severity, tp.getMessage(), tp.getThrowable());
    }

    @Override
    @SuppressWarnings("unused")
    public void debug(final String message) {
    }

    @Override
    @SuppressWarnings("unused")
    public void debug(final String message, final Object arg) {
    }

    @Override
    @SuppressWarnings("unused")
    public void debug(final String message, final Object[] args) {
    }

    @Override
    @SuppressWarnings("unused")
    public void debug(final String message, final Throwable throwable) {
    }

    @Override
    @SuppressWarnings("unused")
    public void debug(final String message, final Object arg1,
            final Object arg2) {
    }

    @Override
    public void error(final String message) {
        log(IStatus.ERROR, message, null);
    }

    @Override
    public void error(final String message, final Object arg) {
        formatAndLog(IStatus.ERROR, message, arg, null);
    }

    @Override
    public void error(final String message, final Object[] args) {
        formatAndLog(IStatus.ERROR, message, args);
    }

    @Override
    public void error(final String message, final Throwable throwable) {
        log(IStatus.ERROR, message, throwable);
    }

    @Override
    public void error(final String message, final Object arg1,
            final Object arg2) {
        formatAndLog(IStatus.ERROR, message, arg1, arg2);
    }

    @Override
    public void info(final String message) {
        log(IStatus.INFO, message, null);
    }

    @Override
    public void info(final String message, final Object arg) {
        formatAndLog(IStatus.INFO, message, arg, null);
    }

    @Override
    public void info(final String message, final Object[] args) {
        formatAndLog(IStatus.INFO, message, args);
    }

    @Override
    public void info(final String message, final Throwable throwable) {
        log(IStatus.INFO, message, throwable);
    }

    @Override
    public void info(final String message, final Object arg1,
            final Object arg2) {
        formatAndLog(IStatus.INFO, message, arg1, arg2);
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public void trace(final String message) {
        log(IStatus.INFO, message, null);
    }

    @Override
    @SuppressWarnings("unused")
    public void trace(final String message, final Object arg) {
    }

    @Override
    @SuppressWarnings("unused")
    public void trace(final String message, final Object[] args) {
    }

    @Override
    @SuppressWarnings("unused")
    public void trace(final String message, final Throwable throwable) {
    }

    @Override
    @SuppressWarnings("unused")
    public void trace(final String message, final Object arg1,
            final Object arg2) {
    }

    @Override
    public void warn(final String message) {
        log(IStatus.WARNING, message, null);
    }

    @Override
    public void warn(final String message, final Object arg) {
        formatAndLog(IStatus.WARNING, message, arg, null);
    }

    @Override
    public void warn(final String message, final Object[] args) {
        formatAndLog(IStatus.WARNING, message, args);
    }

    @Override
    public void warn(final String message, final Throwable throwable) {
        log(IStatus.WARNING, message, throwable);
    }

    @Override
    public void warn(final String message, final Object arg1,
            final Object arg2) {
        formatAndLog(IStatus.INFO, message, arg1, arg2);
    }
}
