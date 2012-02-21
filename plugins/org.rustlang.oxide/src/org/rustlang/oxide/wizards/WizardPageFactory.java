package org.rustlang.oxide.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;

public interface WizardPageFactory {
    RustNewProjectWizardPage create(IStructuredSelection selection);
}
