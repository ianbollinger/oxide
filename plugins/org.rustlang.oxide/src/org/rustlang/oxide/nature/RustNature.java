package org.rustlang.oxide.nature;

import com.google.common.collect.ObjectArrays;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.rustlang.oxide.builder.RustBuilder;

public class RustNature implements IProjectNature {
    public static final String ID = "org.rustlang.oxide.RustNature";
    private IProject project;

    @Override
    public void configure() throws CoreException {
        final IProjectDescription description = project.getDescription();
        final ICommand[] commands = description.getBuildSpec();
        for (final ICommand command : commands) {
            if (command.getBuilderName().equals(RustBuilder.BUILDER_ID)) {
                return;
            }
        }
        final ICommand command = description.newCommand();
        command.setBuilderName(RustBuilder.BUILDER_ID);
        description.setBuildSpec(ObjectArrays.concat(commands, command));
        project.setDescription(description, null);
    }

    @Override
    public void deconfigure() throws CoreException {
        final IProjectDescription description = getProject().getDescription();
        final ICommand[] commands = description.getBuildSpec();
        for (int i = 0; i < commands.length; ++i) {
            if (RustBuilder.BUILDER_ID.equals(commands[i].getBuilderName())) {
                description.setBuildSpec(arrayRemove(commands, i));
                project.setDescription(description, null);
                return;
            }
        }
    }

    // TODO: move somewhere else.
    private static <T> T[] arrayRemove(final T[] array, final int index) {
        final T[] result = ObjectArrays.newArray(array, array.length - 1);
        System.arraycopy(array, 0, result, 0, index);
        System.arraycopy(array, index + 1, result, index, array.length - index
                - 1);
        return result;
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
