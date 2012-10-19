package org.rustlang.oxide.common.launch;

import javax.annotation.concurrent.Immutable;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.Launch;

/**
 * TODO: Document class.
 */
@Immutable
public class LaunchFactory {
    // TODO: make package private.
    public LaunchFactory() {
    }

    /**
     * TODO: Document method.
     *
     * @param configuration
     * @param mode
     * @return
     */
    public ILaunch create(final ILaunchConfiguration configuration,
            final String mode) {
        return new Launch(configuration, mode, null);
    }
}
