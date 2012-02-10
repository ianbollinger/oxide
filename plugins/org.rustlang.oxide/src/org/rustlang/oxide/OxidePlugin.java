package org.rustlang.oxide;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class OxidePlugin extends AbstractUIPlugin {
    public static final String PLUGIN_ID = "org.rustlang.oxide";
    private static OxidePlugin plugin;
    private static TemplateStore templateStore;

    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        templateStore = createTemplateStore();
    }

    private TemplateStore createTemplateStore() {
        return new ContributionTemplateStore(getDefault().getPreferenceStore(),
                "RUST_TEMPLATE_STORE");
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        plugin = null;
        templateStore = null;
        super.stop(context);
    }

    public static OxidePlugin getDefault() {
        return plugin;
    }

    public static TemplateStore getTemplateStore() {
        return templateStore;
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
