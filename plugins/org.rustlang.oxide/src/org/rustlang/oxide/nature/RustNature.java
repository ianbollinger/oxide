package org.rustlang.oxide.nature;

import com.google.common.collect.ObjectArrays;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.rustlang.oxide.builder.RustBuilder;
import org.rustlang.oxide.common.ObjectArrays2;

public class RustNature implements IProjectNature {
    public static final String ID = "org.rustlang.oxide.RustNature";
    private volatile IProject project;

    @Override
    public void configure() throws CoreException {
        final IProjectDescription description = project.getDescription();
        final ICommand[] commands = description.getBuildSpec();
        if (findBuilder(commands) == -1) {
            final ICommand command = description.newCommand();
            command.setBuilderName(RustBuilder.ID);
            setBuildSpec(description, ObjectArrays.concat(commands, command));
        }
    }

    @Override
    public void deconfigure() throws CoreException {
        final IProjectDescription description = project.getDescription();
        final ICommand[] commands = description.getBuildSpec();
        final int index = findBuilder(commands);
        if (index != -1) {
            setBuildSpec(description, ObjectArrays2.remove(commands, index));
        }
    }

    private int findBuilder(final ICommand[] commands) {
        for (int i = 0; i < commands.length; ++i) {
            if (RustBuilder.ID.equals(commands[i].getBuilderName())) {
                return i;
            }
        }
        return -1;
    }

    private void setBuildSpec(final IProjectDescription description,
            final ICommand[] buildSpec) throws CoreException {
        description.setBuildSpec(buildSpec);
        project.setDescription(description, null);
    }

    @Override
    public IProject getProject() {
        return project;
    }

    @Override
    public void setProject(final IProject project) {
        synchronized (this) {
            this.project = project;
        }
    }
}
