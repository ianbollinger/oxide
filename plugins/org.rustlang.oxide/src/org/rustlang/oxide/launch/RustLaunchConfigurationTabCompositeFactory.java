package org.rustlang.oxide.launch;

import com.google.inject.Inject;
import org.eclipse.swt.widgets.Composite;

// TODO: make name less hilarious.
/**
 * TODO: Document class.
 */
public class RustLaunchConfigurationTabCompositeFactory {
    @Inject
    RustLaunchConfigurationTabCompositeFactory() {
    }

    RustLaunchConfigurationTabComposite create(final Composite parent,
            final RustLaunchConfigurationTab tab) {
        return new RustLaunchConfigurationTabComposite(parent, tab);
    }
}
