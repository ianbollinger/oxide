package org.rustlang.oxide.launch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.rustlang.oxide.OxidePlugin;
import org.rustlang.oxide.common.EclipseLogger;

public class RustLaunchConfigurationTab extends AbstractLaunchConfigurationTab {
    private RustLaunchConfigurationTabComposite control;
    private final EclipseLogger logger;

    public RustLaunchConfigurationTab() {
        // TODO: inject.
        this.logger = OxidePlugin.getLogger();
    }

    @Override
    public void createControl(final Composite parent) {
        // TODO: make a factory.
        control = new RustLaunchConfigurationTabComposite(parent, this);
    }

    @Override
    public void dispose() {
        if (control != null) {
            control.dispose();
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
            // TODO: this is a bit hacky. Should we wrap the
            // ILaunchConfigration?
            final String projectName = configuration.getAttribute(
                    RustLaunchAttribute.PROJECT.toString(), "");
            final String executable = configuration.getAttribute(
                    RustLaunchAttribute.EXECUTABLE.toString(), "");
            final String programArguments = configuration.getAttribute(
                    RustLaunchAttribute.ARGUMENTS.toString(), "");
            control.setProject(projectName);
            control.setExecutable(executable);
            control.setProgramArguments(programArguments);
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
        // TODO: LoD violation.
        final ILaunchConfigurationDialog dialog =
                getLaunchConfigurationDialog();
        dialog.updateButtons();
        dialog.updateMessage();
    }
}
