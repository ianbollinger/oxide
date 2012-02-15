package org.rustlang.oxide;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.rustlang.oxide.common.EnumPreferenceStore;

public class OxidePlugin extends AbstractUIPlugin {
    public static final String ID = "org.rustlang.oxide";
    private static OxidePlugin plugin;
    private static TemplateStore templateStore;
    private final OxideLogger logger;

    public OxidePlugin() {
        this.logger = new OxideLogger();
    }

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

    public static ImageDescriptor getImageDescriptor(
            final String imageFilePath) {
        return imageDescriptorFromPlugin(ID, imageFilePath);
    }

    public static EnumPreferenceStore getEnumPreferenceStore() { 
        return new EnumPreferenceStore(getDefault().getPreferenceStore());
    }

    public static OxideLogger getLogger() {
        return getDefault().logger;
    }
}
