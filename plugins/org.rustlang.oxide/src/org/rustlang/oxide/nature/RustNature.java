/*
 * Copyright 2012 Ian D. Bollinger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
        assert commands != null;
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
        assert commands != null;
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

    @SuppressWarnings("null")
    @Override
    public IProject getProject() {
        return project;
    }

    @SuppressWarnings("null")
    @Override
    public void setProject(final IProject project) {
        synchronized (this) {
            this.project = project;
        }
    }
}
