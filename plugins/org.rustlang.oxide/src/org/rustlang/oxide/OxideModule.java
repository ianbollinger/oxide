package org.rustlang.oxide;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class OxideModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Plugin.class).to(AbstractUIPlugin.class);
    }

    @Provides
    AbstractUIPlugin providePlugin() {
        return OxidePlugin.getDefault();
    }
}
