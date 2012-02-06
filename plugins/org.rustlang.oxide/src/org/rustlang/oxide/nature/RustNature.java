package org.rustlang.oxide.nature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;

public class RustNature implements IProjectNature {
    private IProject project;

    @Override
    public void configure() {
    }

    @Override
    public void deconfigure() {
    }

    @Override
    public IProject getProject() {
        return project;
    }

    @Override
    public synchronized void setProject(IProject project) {
        this.project = project;
    }
}
