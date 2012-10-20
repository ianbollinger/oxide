package org.rustlang.oxide.common;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * TODO: Document class.
 */
@Immutable
public class StatusFactory {
    StatusFactory() {
    }

    /**
     * TODO: Document method.
     *
     * @param severity
     * @param message
     * @param throwable
     * @return
     */
    public Status create(final int severity, @Nullable final String message,
            @Nullable final Throwable throwable) {
        // TODO: get ID properly.
        return new Status(severity, "org.rustlang.oxide", IStatus.OK, message,
                throwable);
    }
}
