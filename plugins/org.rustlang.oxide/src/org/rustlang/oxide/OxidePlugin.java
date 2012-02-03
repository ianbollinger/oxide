package org.rustlang.oxide;

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

    public static ImageDescriptor getImageDescriptor(
            final String imageFilePath) {
        return imageDescriptorFromPlugin(PLUGIN_ID, imageFilePath);
    }
}
