package org.rustlang.oxide.templates;

import java.io.IOException;
import com.google.common.base.Throwables;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;

public class RustTemplateModule extends AbstractModule {
    // TODO: make sure key is named appropriately.
    private static final String RUST_TEMPLATE_STORE_KEY = "RUST_TEMPLATE_STORE";

    @Override
    protected void configure() {
        bind(TemplateContext.class).to(BasicTemplateContext.class);
        bind(TemplateContextType.class).to(RustTemplateContextType.class);
        install(new FactoryModuleBuilder()
                .implement(TemplateFileWriter.class,
                        TemplateFileWriter.class)
                .build(TemplateFileWriterFactory.class));
    }

    @Provides @Singleton
    TemplateStore provideTemplateStore(final IPreferenceStore preferenceStore) {
        final ContributionTemplateStore store = new ContributionTemplateStore(
                preferenceStore, RUST_TEMPLATE_STORE_KEY);
        try {
            store.load();
        } catch (final IOException e) {
            throw Throwables.propagate(e);
        }
        return store;
    }
}
