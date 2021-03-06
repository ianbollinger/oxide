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

package org.rustlang.oxide.launch;

import com.google.inject.Inject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.slf4j.Logger;

/**
 * TODO: Document class.
 */
public class RustLaunchConfigurationTab extends AbstractLaunchConfigurationTab {
    private final RustLaunchConfigurationTabCompositeFactory factory;
    private final Logger logger;
    // TODO: ensure thread safety.
    private volatile RustLaunchConfigurationTabComposite control;

    @Inject
    RustLaunchConfigurationTab(
            final RustLaunchConfigurationTabCompositeFactory factory,
            final Logger logger) {
        this.factory = factory;
        this.logger = logger;
    }

    @Override
    public void createControl(final Composite parent) {
        control = factory.create(parent, this);
    }

    @Override
    public void dispose() {
        if (control != null) {
            control.dispose();
            control = null;
        }
    }

    @Override
    public Control getControl() {
        return control;
    }

    @Override
    public void setDefaults(
            final ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(RustLaunchAttribute.PROJECT.toString(), "");
        configuration.setAttribute(RustLaunchAttribute.EXECUTABLE.toString(),
                "");
        configuration.setAttribute(RustLaunchAttribute.ARGUMENTS.toString(),
                "");
    }

    @Override
    public void initializeFrom(final ILaunchConfiguration configuration) {
        try {
            configure(configuration);
        } catch (final CoreException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void performApply(
            final ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(RustLaunchAttribute.PROJECT.toString(),
                control.getProject());
        configuration.setAttribute(RustLaunchAttribute.EXECUTABLE.toString(),
                control.getExecutable());
        configuration.setAttribute(RustLaunchAttribute.ARGUMENTS.toString(),
                control.getProgramArguments());
        configuration.setAttribute(DebugPlugin.ATTR_CONSOLE_ENCODING, "UTF-8");
    }

    @Override
    public String getName() {
        return "Main";
    }

    public void update() {
        // validate();
        final ILaunchConfigurationDialog dialog =
                getLaunchConfigurationDialog();
        dialog.updateButtons();
        dialog.updateMessage();
    }

    private void configure(
            final ILaunchConfiguration configuration) throws CoreException {
        setProject(configuration);
        setExecutable(configuration);
        setProgramArguments(configuration);
    }

    private void setProject(
            final ILaunchConfiguration configuration) throws CoreException {
        // TODO: this is a bit hacky. Should we wrap the
        // ILaunchConfigration?
        final String projectName = configuration.getAttribute(
                RustLaunchAttribute.PROJECT.toString(), "");
        control.setProject(projectName);
    }

    private void setExecutable(
            final ILaunchConfiguration configuration) throws CoreException {
        final String executable = configuration.getAttribute(
                RustLaunchAttribute.EXECUTABLE.toString(), "");
        control.setExecutable(executable);
    }

    private void setProgramArguments(
            final ILaunchConfiguration configuration) throws CoreException {
        final String programArguments = configuration.getAttribute(
                RustLaunchAttribute.ARGUMENTS.toString(), "");
        control.setProgramArguments(programArguments);
    }
}
