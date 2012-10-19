package org.rustlang.oxide.launch;

import org.eclipse.swt.widgets.Composite;

// TODO: make name less hilarious.
/**
 * TODO: Document.
 */
public class RustLaunchConfigurationTabCompositeFactory {
    RustLaunchConfigurationTabCompositeFactory() {
    }

    RustLaunchConfigurationTabComposite create(final Composite parent,
            final RustLaunchConfigurationTab tab) {
        return new RustLaunchConfigurationTabComposite(parent, tab);
    }
}
