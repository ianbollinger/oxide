package org.rustlang.oxide;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ILog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
import org.rustlang.oxide.templates.BasicTemplateContext;
import org.rustlang.oxide.templates.RustTemplateContextType;

public class OxideModule extends AbstractModule {
    @Override
    protected void configure() {
        // TODO: what restriction?
        bind(TemplateContext.class).to(BasicTemplateContext.class);
        bind(TemplateContextType.class).to(RustTemplateContextType.class);
    }

    @Provides
    IPreferenceStore providePreferenceStore(final OxidePlugin plugin) {
        return plugin.getPreferenceStore();
    }

    @Provides @Singleton
    TemplateStore provideTemplateStore(final IPreferenceStore preferenceStore) {
        // TODO: store string constant somewhere, and make sure it's named
        // appropriate.
        return new ContributionTemplateStore(preferenceStore,
                "RUST_TEMPLATE_STORE");
    }

    @Provides @Singleton
    ILog provideLog(final OxidePlugin plugin) {
        return plugin.getLog();
    }

    @Provides
    IWorkspace provideWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }

    @Provides
    OxidePlugin providePlugin() {
        return OxidePlugin.getDefault();
    }
}
