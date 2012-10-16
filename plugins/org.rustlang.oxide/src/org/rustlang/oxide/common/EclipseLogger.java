/*
 * Copyright 2012 Ian D. Bollinger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.rustlang.oxide.common;

import javax.annotation.Nullable;

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

    private void log(final int severity, @Nullable final String message,
            @Nullable final Throwable throwable) {
        wrappedLog.log(provideStatus(severity, message, throwable));
    }

    Status provideStatus(final int severity, @Nullable final String message,
            @Nullable final Throwable throwable) {
        // TODO: get this from a factory.
        return new Status(severity, OxidePlugin.ID, IStatus.OK, message,
                throwable);
    }

    private void formatAndLog(final int severity, @Nullable final String format,
            @Nullable final Object arg1, @Nullable final Object arg2) {
        final FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
        log(severity, tp.getMessage(), tp.getThrowable());
    }

    private void formatAndLog(final int severity, @Nullable final String format,
            @Nullable final Object[] args) {
        final FormattingTuple tp = MessageFormatter.arrayFormat(format, args);
        log(severity, tp.getMessage(), tp.getThrowable());
    }

    @Override
    @SuppressWarnings("unused")
    public void debug(@Nullable final String message) {
    }

    @Override
    @SuppressWarnings("unused")
    public void debug(@Nullable final String message,
            @Nullable final Object arg) {
    }

    @Override
    @SuppressWarnings("unused")
    public void debug(@Nullable final String message,
            @Nullable final Object[] args) {
    }

    @Override
    @SuppressWarnings("unused")
    public void debug(@Nullable final String message,
            @Nullable final Throwable throwable) {
    }

    @Override
    @SuppressWarnings("unused")
    public void debug(@Nullable final String message,
            @Nullable final Object arg1, @Nullable final Object arg2) {
    }

    @Override
    public void error(@Nullable final String message) {
        log(IStatus.ERROR, message, null);
    }

    @Override
    public void error(@Nullable final String message,
            @Nullable final Object arg) {
        formatAndLog(IStatus.ERROR, message, arg, null);
    }

    @Override
    public void error(@Nullable final String message,
            @Nullable final Object[] args) {
        formatAndLog(IStatus.ERROR, message, args);
    }

    @Override
    public void error(@Nullable final String message,
            @Nullable final Throwable throwable) {
        log(IStatus.ERROR, message, throwable);
    }

    @Override
    public void error(@Nullable final String message,
            @Nullable final Object arg1, @Nullable final Object arg2) {
        formatAndLog(IStatus.ERROR, message, arg1, arg2);
    }

    @Override
    public void info(@Nullable final String message) {
        log(IStatus.INFO, message, null);
    }

    @Override
    public void info(@Nullable final String message,
            @Nullable final Object arg) {
        formatAndLog(IStatus.INFO, message, arg, null);
    }

    @Override
    public void info(@Nullable final String message,
            @Nullable final Object[] args) {
        formatAndLog(IStatus.INFO, message, args);
    }

    @Override
    public void info(@Nullable final String message,
            @Nullable final Throwable throwable) {
        log(IStatus.INFO, message, throwable);
    }

    @Override
    public void info(@Nullable final String message,
            @Nullable final Object arg1, @Nullable final Object arg2) {
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
    public void trace(@Nullable final String message) {
        log(IStatus.INFO, message, null);
    }

    @Override
    @SuppressWarnings("unused")
    public void trace(@Nullable final String message,
            @Nullable final Object arg) {
    }

    @Override
    @SuppressWarnings("unused")
    public void trace(@Nullable final String message,
            @Nullable final Object[] args) {
    }

    @Override
    @SuppressWarnings("unused")
    public void trace(@Nullable final String message,
            @Nullable final Throwable throwable) {
    }

    @Override
    @SuppressWarnings("unused")
    public void trace(@Nullable final String message,
            @Nullable final Object arg1, @Nullable final Object arg2) {
    }

    @Override
    public void warn(@Nullable final String message) {
        log(IStatus.WARNING, message, null);
    }

    @Override
    public void warn(@Nullable final String message,
            @Nullable final Object arg) {
        formatAndLog(IStatus.WARNING, message, arg, null);
    }

    @Override
    public void warn(@Nullable final String message,
            @Nullable final Object[] args) {
        formatAndLog(IStatus.WARNING, message, args);
    }

    @Override
    public void warn(@Nullable final String message,
            @Nullable final Throwable throwable) {
        log(IStatus.WARNING, message, throwable);
    }

    @Override
    public void warn(@Nullable final String message,
            @Nullable final Object arg1, @Nullable final Object arg2) {
        formatAndLog(IStatus.INFO, message, arg1, arg2);
    }
}
