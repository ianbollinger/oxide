package org.rustlang.oxide;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.rustlang.oxide.common.EnumPreferenceStore;

public class OxidePlugin extends AbstractUIPlugin {
    public static final String ID = "org.rustlang.oxide";
    private static OxidePlugin plugin;
    private Injector injector;

    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);
        injector = Guice.createInjector(new OxideModule());
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
        return imageDescriptorFromPlugin(ID, imageFilePath);
    }

    public static OxideLogger getLogger() {
        return getInstance(OxideLogger.class);
    }

    public static EnumPreferenceStore getEnumPreferenceStore() {
        return getInstance(EnumPreferenceStore.class);
    }

    public static <T> T getInstance(final Class<T> clazz) {
        return plugin.injector.getInstance(clazz);
    }
}
