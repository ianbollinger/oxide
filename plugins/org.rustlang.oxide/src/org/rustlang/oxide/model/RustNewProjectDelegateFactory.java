package org.rustlang.oxide.model;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.sapphire.modeling.ProgressMonitor;

public interface RustNewProjectDelegateFactory {
    RustNewProjectDelegate create(RustProjectOperationModel element,
            IConfigurationElement configuration, ProgressMonitor monitor);
}
