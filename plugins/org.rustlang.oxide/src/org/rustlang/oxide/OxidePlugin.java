package org.rustlang.oxide;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class OxidePlugin extends AbstractUIPlugin {
    public static final String PLUGIN_ID = "org.rustlang.oxide";
    private static OxidePlugin plugin;

    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static OxidePlugin getDefault() {
        return plugin;
    }

    public static ImageDescriptor getImageDescriptor(final String imageFilePath) {
        return imageDescriptorFromPlugin(PLUGIN_ID, imageFilePath);
    }

    // TODO: move logging somewhere more appropriate.
    public static void log(final Throwable throwable) {
        log(null, throwable);
    }

    public static void log(final String message) {
        log(message, null);
    }

    public static void log(final String message, final Throwable throwable) {
        log(IStatus.INFO, message, throwable);
    }

    public static void log(final int severity, final String message,
            final Throwable throwable) {
        final Status status = new Status(severity, PLUGIN_ID, IStatus.OK,
                message, throwable);
        getDefault().getLog().log(status);
    }
}
