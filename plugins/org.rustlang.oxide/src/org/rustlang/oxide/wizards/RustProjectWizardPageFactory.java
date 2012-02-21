package org.rustlang.oxide.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;

public interface RustProjectWizardPageFactory {
    RustProjectWizardPage create(IStructuredSelection selection);
}
